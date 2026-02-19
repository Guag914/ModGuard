package net.guag.modguard.modrinth;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstallerScreen extends Screen {

    public InstallerScreen(Text title) {
        super(title);
    }

    private TextFieldWidget searchBox;
    private String searchQuery = "";

    private TextFieldWidget limitEntry;
    private String limitQuery = "100";

    private String offsetQuery = "0";

    private String finalJSON;

    //Scrolling
    int ys; /**ONLY ADD HEIGHT AND OFFSET FOR PROPER SPACING**/

    ButtonWidget API_CALL;
    ButtonWidget NEXT_PAGE;
    ButtonWidget PREV_PAGE;

    Integer pageNum;

    //facets
    private final List<CustomFacetButton> facetButtons = new ArrayList<>();

    @Override
    protected void init() {
        ys+=5 - offsetY;

        this.searchBox = new TextFieldWidget(this.textRenderer, 30 - offsetX, ys, this.width-35, 20, Text.of("Search Box"));
        this.searchBox.setChangedListener(query -> { this.searchQuery = query.toLowerCase(); });
        /**ATTRIBUTES**/this.searchBox.setMaxLength(100); this.searchBox.setEditable(true); this.addSelectableChild(this.searchBox); this.addDrawableChild(this.searchBox); this.searchBox.setDrawsBackground(false);

        ys+=20 - offsetY;

        this.limitEntry = new TextFieldWidget(this.textRenderer, 5 - offsetX, ys, 20, 20, Text.of("Limit Entry Box"));
        this.limitEntry.setChangedListener(query -> { this.limitQuery = query.toLowerCase(); });
        /**ATTRIBUTES**/this.limitEntry.setMaxLength(2); this.limitEntry.setEditable(true); this.addSelectableChild(this.limitEntry); this.addDrawableChild(this.limitEntry); this.limitEntry.setDrawsBackground(false);

        ys+=20-offsetY;

        API_CALL = ButtonWidget.builder(Text.translatable("button.ModGuard.callAPI"),
                button -> {this.run();}
        ).dimensions(this.width / 2 - 100, ys, 200, 20).build();
        addDrawableChild(API_CALL);

        ys+=20-offsetY;

        PREV_PAGE = ButtonWidget.builder(Text.translatable("button.ModGuard.prevpg"),
                button -> { pageNum--; updateOffset(); this.run();}
        ).dimensions(5, ys, 50, 20).build();
        addDrawableChild(PREV_PAGE);

        NEXT_PAGE = ButtonWidget.builder(Text.translatable("button.ModGuard.nextpg"),
                button -> { pageNum++; updateOffset(); this.run();}
        ).dimensions(this.width-55, ys, 50, 20).build();
        addDrawableChild(NEXT_PAGE);

        ys+=20-offsetY;

        CustomFacetButton license_mit = new CustomFacetButton(5, ys, 10, false, "license", "mit", "MIT License"); facetButtons.add(license_mit); addDrawableChild(license_mit);

        maxScroll = Math.max(0, ys+200 /** change content height to scroll less/more on screen**/ - (this.height - 80));
    }

    //HANDLE CLICKING ENTER FOR SEARCH
    private boolean enterWasDown = false;

    @Override
    public void tick() {
        super.tick();
        boolean enterDown = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS;
        if (enterDown && !enterWasDown) { run(); }
        enterWasDown = enterDown;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        /**change y levels by this to make scrolling happen**/ ys = -(int)scrollAmount;

        //FIX CONSOLE STYLE CURSOR LATER
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of(finalJSON), 0, this.height-30, 0xFFFFFFFF);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("modguard", "textures/gui/modrinthfavicon.png"), 5, ys+5, 0, 0, 20, 20, 20, 20);

        drawTextFieldBackground(context, searchBox);
        drawTextFieldBackground(context, limitEntry);

        drawTextFieldWithPlaceholder(context, searchBox, "Search mods");
        drawTextFieldWithPlaceholder(context, limitEntry, "50");

        //Update button/widget positions (for scrolling)
        ys+=5 - offsetY; //Offset from top

        searchBox.setY(ys);
        ys+=20-offsetY; limitEntry.setY(ys);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of("mods per page."), (5 - offsetX)+20/*+(limitEntry.getText().length()*5)*/, ys, 0xFF8c8f8d);

        ys+=20-offsetY; API_CALL.setY(ys);
        ys+=20-offsetY; NEXT_PAGE.setY(ys); PREV_PAGE.setY(ys);

        //handle all toggle buttons in arraylist
        for (int i = 0; i < facetButtons.size(); i++){ ys+=20-offsetY; facetButtons.get(i).setY(ys); }

        super.render(context, mouseX, mouseY, delta); //ALWAYS PUT THIS LAST SO YOU DON'T DRAW ON TOP
    }

    public void run(){
        try {

            ModrinthAPIFetch fetcher = new ModrinthAPIFetch();
            // Pass "project" to trigger your current initRequest logic

            if (searchQuery.isBlank()){ searchQuery = "*"; } //* works for blank query //blank space may also work but safer to use *
            fetcher.setQuery(searchQuery);

            if (limitQuery.isBlank()){ limitQuery = String.valueOf(50); }
            fetcher.setLimit(Integer.valueOf(limitQuery));

            if (offsetQuery.isBlank()){ offsetQuery = String.valueOf(0); }
            fetcher.setOffset(Integer.valueOf(offsetQuery));

            for (int i = 0; i < facetButtons.size(); i++){
                if (facetButtons.get(i).isToggled()){ fetcher.putFacet(facetButtons.get(i).name(), facetButtons.get(i).content()); }
            }

            finalJSON = fetcher.initRequest("project");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**the offset is doubled because the boxes are moved relatively while the text boxes are moved inversely**/
    int offsetY = -7;
    int offsetX = -5;

    //Styling for Text Widgets:
    private void drawTextFieldBackground(DrawContext context, TextFieldWidget field) {
        int x = field.getX();
        int y = field.getY();
        int w = field.getWidth();
        int h = field.getHeight();

        int bgColor = 0x802B2B2B; // dark background

        // Background
        context.fill(x + offsetX, y + offsetY, x + w + offsetX, y + h + offsetY, bgColor);
    }

    private void drawTextFieldWithPlaceholder(DrawContext context, TextFieldWidget field, String placeholder) {
        // Render the text normally
        field.render(context, 0, 0, 0f);

        // If the field is empty, draw the placeholder in white
        if (field.getText().isEmpty()) {
            int x = field.getX() + 4 + offsetX; // padding inside the box
            int y = (field.getY() + (field.getHeight() - this.textRenderer.fontHeight) / 2) + offsetY+2; // vertical center
            context.drawText(this.textRenderer, Text.literal(placeholder), x, y, 0xFF8c8f8d, false);
        }
    }

    //Scroll handling
    private double scrollAmount = 0;
    private double maxScroll = 0;
    private final double scrollStep = 15;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollAmount -= verticalAmount * scrollStep;
        scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        return true;
    }

    //PAGE HANDLING
    public void setPage(Integer p){ this.pageNum = p; }
    public int getPage(){ return pageNum; }
    private void updateOffset(){ offsetQuery = String.valueOf(Integer.valueOf(limitQuery)*(pageNum-1)); System.out.println(offsetQuery); }

}