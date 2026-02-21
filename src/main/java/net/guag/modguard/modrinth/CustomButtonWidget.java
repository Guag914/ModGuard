package net.guag.modguard.modrinth;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CustomButtonWidget extends ButtonWidget {

    private final int normalColor = 0x802B2B2B;
    private final int hoverColor = 0x802B2B2B;

    protected CustomButtonWidget(int x, int y, int width, int height, String message, PressAction onPress) {
        super(x, y, width, height, net.minecraft.text.Text.translatable(message), onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int color = this.isHovered() ? hoverColor : normalColor;
        int x = getX();
        int y = getY();
        int w = getWidth();

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = renderer.getWidth(this.getMessage());
        int calcX = x + (w - textWidth) / 2;

        int textY = y + (getHeight() - renderer.fontHeight) / 2;
        // main fill
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color);

        // border (optional)
        context.fill(getX(), getY(), getX() + getWidth(), getY() + 1, 0x802B2B2B);           // top
        context.fill(getX(), getY() + getHeight() - 1, getX() + getWidth(), getY() + getHeight(), 0x802B2B2B); // bottom
        context.fill(getX(), getY(), getX() + 1, getY() + getHeight(), 0x802B2B2B);           // left
        context.fill(getX() + getWidth() - 1, getY(), getX() + getWidth(), getY() + getHeight(), 0x802B2B2B); // right

        context.drawTextWithShadow(renderer, this.getMessage(), calcX, textY, 0xFF8c8f8d);

    }
}