package com.bilicraft.mantle_fabric.client.screen.book.element;

import com.bilicraft.mantle_fabric.client.book.action.StringActionProcessor;
import com.bilicraft.mantle_fabric.client.book.data.element.TextComponentData;
import com.bilicraft.mantle_fabric.client.screen.book.TextComponentDataRenderer;
import com.bilicraft.mantle_fabric.client.screen.book.TextDataRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TextComponentElement extends SizedBookElement {

  public TextComponentData[] text;
  private final List<ITextComponent> tooltip = new ArrayList<ITextComponent>();

  private boolean doAction = false;

  public TextComponentElement(int x, int y, int width, int height, String text) {
    this(x, y, width, height, new StringTextComponent(text));
  }

  public TextComponentElement(int x, int y, int width, int height, ITextComponent text) {
    this(x, y, width, height, new TextComponentData(text));
  }

  public TextComponentElement(int x, int y, int width, int height, Collection<TextComponentData> text) {
    this(x, y, width, height, text.toArray(new TextComponentData[0]));
  }

  public TextComponentElement(int x, int y, int width, int height, TextComponentData... text) {
    super(x, y, width, height);

    this.text = text;
  }

  @Override
  public void draw(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
    String action = TextComponentDataRenderer.drawText(matrixStack, this.x, this.y, this.width, this.height, this.text, mouseX, mouseY, fontRenderer, this.tooltip);

    if (this.doAction) {
      this.doAction = false;
      StringActionProcessor.process(action, this.parent);
    }
  }

  @Override
  public void drawOverlay(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
    if (this.tooltip.size() > 0) {
      TextDataRenderer.drawTooltip(matrixStack, this.tooltip, mouseX, mouseY, fontRenderer);
      this.tooltip.clear();
    }
  }

  @Override
  public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
    if (mouseButton == 0) {
      this.doAction = true;
    }
  }
}
