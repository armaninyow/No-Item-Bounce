package com.armaninyow.noitembounce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ConfigScreen::new;
	}

	public static class ConfigScreen extends Screen {
		private final Screen parent;
		private ButtonWidget toggleButton;

		public ConfigScreen(Screen parent) {
			super(Text.literal("No Item Bounce Config"));
			this.parent = parent;
		}

		@Override
		protected void init() {
			// Toggle button for vertical bounce
			this.toggleButton = ButtonWidget.builder(
				getToggleButtonText(),
				button -> {
					NoItemBounce.setRemoveVerticalBounce(!NoItemBounce.shouldRemoveVerticalBounce());
					button.setMessage(getToggleButtonText());
				})
				.dimensions(this.width / 2 - 100, this.height / 2 - 10, 200, 20)
				.build();
			this.addDrawableChild(this.toggleButton);

			// Done button
			this.addDrawableChild(ButtonWidget.builder(
				ScreenTexts.DONE,
				button -> {
					if (this.client != null) {
						this.client.setScreen(this.parent);
					}
				})
				.dimensions(this.width / 2 - 100, this.height / 2 + 30, 200, 20)
				.build());
		}

		private Text getToggleButtonText() {
			boolean isRemoved = NoItemBounce.shouldRemoveVerticalBounce();
			return Text.literal("Remove Vertical Bounce: " + (isRemoved ? "§aON" : "§cOFF"));
		}

		@Override
		public void render(DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			// Title
			context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
			
			// Description
			String desc1 = "Items always spawn centered on X/Z axes";
			String desc2 = "Toggle removes the upward 'pop' when items drop";
			context.drawCenteredTextWithShadow(this.textRenderer, desc1, this.width / 2, this.height / 2 - 40, 0xAAAAAA);
			context.drawCenteredTextWithShadow(this.textRenderer, desc2, this.width / 2, this.height / 2 - 28, 0xAAAAAA);
		}

		@Override
		public void close() {
			if (this.client != null) {
				this.client.setScreen(this.parent);
			}
		}
	}
}