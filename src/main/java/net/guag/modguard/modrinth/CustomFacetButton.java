package net.guag.modguard.modrinth;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CustomFacetButton extends ButtonWidget {

    private boolean toggled;
    private String name;
    private String content;
    private String displayName;

    public CustomFacetButton(int x, int y, int size, boolean initialState, String n, String c, String dn) {
        super(
                x,
                y,
                size,
                size,
                net.minecraft.text.Text.of(""),
                button -> ((CustomFacetButton) button).setOppositeToggled(),
                DEFAULT_NARRATION_SUPPLIER
        );

        this.toggled = initialState;
        this.name = n;
        this.content = c;
        this.displayName = dn;
    }

    public boolean isToggled() { return toggled; }

    public String name(){ return this.name; }
    public String content(){ return this.content; }
    public String displayName(){ return this.displayName; }

    public void setOppositeToggled() { this.toggled = !this.toggled; }

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        int fillColor = toggled ? 0xFF8c8f8d : 0x00000000;

        // Main square
        context.fill(x+2, y+2, x + w-2, y + h-2, fillColor);

        // Border (1px white outline)
        context.fill(x, y, x + w, y + 1, 0xFF8c8f8d);           // top
        context.fill(x, y + h - 1, x + w, y + h, 0xFF8c8f8d);   // bottom
        context.fill(x, y, x + 1, y + h, 0xFF8c8f8d);           // left
        context.fill(x + w - 1, y, x + w, y + h, 0xFF8c8f8d);   // right

        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, net.minecraft.text.Text.of(this.displayName), x+w+5, y+1, 0xFF8c8f8d);

    }
}