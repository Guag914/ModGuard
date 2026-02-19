package net.guag.modguard.modrinth;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class InstallerScreen extends Screen {

    public InstallerScreen(Text title) {
        super(title);
    }

    private Identifier texture = Identifier.of("modguard", "textures/gui/opengui.png");

    private TextFieldWidget searchBox;
    private String searchQuery = "";

    private TextFieldWidget limitEntry;
    private String limitQuery = "100";

    private TextFieldWidget offsetEntry;
    private String offsetQuery = "0";

    private String finalJSON;

    @Override
    protected void init() {
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("button.ModGuard.callAPI"), button -> this.run())
                        .dimensions(this.width / 2 - 100, this.height/2, 200, 20)
                        .build()
        );

        this.searchBox = new TextFieldWidget(this.textRenderer, 30, 5, this.width-35, 20, Text.of("Search Box"));
        this.searchBox.setChangedListener(query -> { this.searchQuery = query.toLowerCase(); });
        this.searchBox.setMaxLength(100);
        this.searchBox.setEditable(true);
        this.searchBox.setTooltip(Tooltip.of(Text.of("Type to search modrinth...")));
        this.addSelectableChild(this.searchBox);
        this.setInitialFocus(this.searchBox);

        this.limitEntry = new TextFieldWidget(this.textRenderer, 5, 30, (this.width/2)-10, 20, Text.of("Limit Entry Box"));
        this.limitEntry.setChangedListener(query -> { this.limitQuery = query.toLowerCase(); });
        this.limitEntry.setMaxLength(100);
        this.limitEntry.setEditable(true);
        this.limitEntry.setTooltip(Tooltip.of(Text.of("Limit the number of search results...")));
        this.addSelectableChild(this.limitEntry);

        this.offsetEntry = new TextFieldWidget(this.textRenderer, this.width/2, 30, this.width/2-5, 20, Text.of("Offset Entry Box"));
        this.offsetEntry.setChangedListener(query -> { this.offsetQuery = query.toLowerCase(); });
        this.offsetEntry.setMaxLength(100);
        this.offsetEntry.setEditable(true);
        this.offsetEntry.setTooltip(Tooltip.of(Text.of("Offset the number of search results...")));
        this.addSelectableChild(this.offsetEntry);

        this.addDrawableChild(this.searchBox);
        this.addDrawableChild(this.limitEntry);
        this.addDrawableChild(this.offsetEntry);

        //Styling
        this.searchBox.setDrawsBackground(false);
        this.limitEntry.setDrawsBackground(false);
        this.offsetEntry.setDrawsBackground(false);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);
        context.fill(30, 5, this.width - 5, 25, 0xAA1E1E1E); // dark rounded-style background
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of(finalJSON), 0, this.height-30, 0xFFFFFFFF);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("modguard", "textures/gui/modrinthfavicon.png"), 5, 5, 0, 0, 20, 20, 20, 20);
    }

    public void run(){
        try {

            ModrinthAPIFetch fetcher = new ModrinthAPIFetch();
            // Pass "project" to trigger your current initRequest logic

            fetcher.setQuery(searchQuery);
            fetcher.setLimit(Integer.valueOf(limitQuery));
            fetcher.setOffset(Integer.valueOf(offsetQuery));

            finalJSON = fetcher.initRequest("project");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
