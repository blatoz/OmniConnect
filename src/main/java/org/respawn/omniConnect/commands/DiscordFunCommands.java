package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class DiscordFunCommands extends ListenerAdapter {

    private final Random random = new Random();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (!isFunModuleEnabled()) {
            String lang = LangManager.getDefaultLanguage();
            sendError(event, LangManager.get(lang, "errors.fun_disabled"));
            return;
        }

        String name = event.getName();

        switch (name) {
            case "roll":
                if (isCommandEnabled("roll")) handleRoll(event);
                break;
            case "coinflip":
                if (isCommandEnabled("coinflip")) handleCoinflip(event);
                break;
            case "8ball":
                if (isCommandEnabled("8ball")) handle8Ball(event);
                break;
            case "joke":
                if (isCommandEnabled("joke")) handleJoke(event);
                break;
            case "cat":
                if (isCommandEnabled("cat")) handleCat(event);
                break;
            case "dog":
                if (isCommandEnabled("dog")) handleDog(event);
                break;
            case "hug":
                if (isCommandEnabled("hug")) handleHug(event);
                break;
            case "slap":
                if (isCommandEnabled("slap")) handleSlap(event);
                break;
            case "meme":
                if (isCommandEnabled("meme")) handleMeme(event);
                break;
            case "gif":
                if (isCommandEnabled("gif")) handleGif(event);
                break;
            case "quote":
                if (isCommandEnabled("quote")) handleQuote(event);
                break;
            case "ship":
                if (isCommandEnabled("ship")) handleShip(event);
                break;
            case "lovecalc":
                if (isCommandEnabled("lovecalc")) handleLoveCalc(event);
                break;
        }
    }

    // ---------------------------------------------------------
    // Helper metódusok
    // ---------------------------------------------------------

    private boolean isFunModuleEnabled() {
        return Main.getInstance().getConfig().getBoolean("discord.fun.enabled", true);
    }

    private boolean isCommandEnabled(String command) {
        return Main.getInstance().getConfig().getBoolean("discord.fun.commands." + command, true);
    }

    private Member getMemberOption(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) == null) return null;
        try {
            return event.getOption(name).getAsMember();
        } catch (Exception e) {
            return null;
        }
    }

    private String getStringOption(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) == null) return null;
        try {
            return event.getOption(name).getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    private void sendError(SlashCommandInteractionEvent event, String message) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(message)
                .setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    // ---------------------------------------------------------
    // FUN PARANCSOK
    // ---------------------------------------------------------

    private void handleRoll(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        int max = 100;
        if (event.getOption("max") != null) {
            try {
                max = event.getOption("max").getAsInt();
            } catch (Exception ignored) {}
        }
        if (max <= 0) max = 1;

        int result = random.nextInt(max) + 1;

        String message = LangManager.get(lang, "discord.fun.roll")
                .replace("%number%", String.valueOf(result))
                .replace("%max%", String.valueOf(max));

        event.reply(message).queue();
    }

    private void handleCoinflip(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        String result = random.nextBoolean()
                ? LangManager.get(lang, "discord.fun.coinflip_heads")
                : LangManager.get(lang, "discord.fun.coinflip_tails");

        String message = LangManager.get(lang, "discord.fun.coinflip")
                .replace("%result%", result);

        event.reply(message).queue();
    }

    private void handle8Ball(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        List<String> answers = List.of(
                LangManager.get(lang, "discord.fun.8ball.answer1"),
                LangManager.get(lang, "discord.fun.8ball.answer2"),
                LangManager.get(lang, "discord.fun.8ball.answer3"),
                LangManager.get(lang, "discord.fun.8ball.answer4"),
                LangManager.get(lang, "discord.fun.8ball.answer5"),
                LangManager.get(lang, "discord.fun.8ball.answer6"),
                LangManager.get(lang, "discord.fun.8ball.answer7"),
                LangManager.get(lang, "discord.fun.8ball.answer8")
        );

        String result = answers.get(random.nextInt(answers.size()));

        String message = LangManager.get(lang, "discord.fun.8ball")
                .replace("%answer%", result);

        event.reply(message).queue();
    }

    private void handleJoke(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        List<String> jokes = List.of(
                LangManager.get(lang, "discord.fun.joke1"),
                LangManager.get(lang, "discord.fun.joke2"),
                LangManager.get(lang, "discord.fun.joke3"),
                LangManager.get(lang, "discord.fun.joke4")
        );

        String joke = jokes.get(random.nextInt(jokes.size()));

        String message = LangManager.get(lang, "discord.fun.joke")
                .replace("%joke%", joke);

        event.reply(message).queue();
    }

    private void handleCat(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        List<String> cats = List.of(
                "https://cataas.com/cat",
                "https://cataas.com/cat/cute",
                "https://cataas.com/cat/says/hello"
        );

        String url = cats.get(random.nextInt(cats.size()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.fun.cat_title"))
                .setImage(url)
                .setColor(Color.PINK)
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }

    private void handleDog(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        List<String> dogs = List.of(
                "https://random.dog/woof.jpg",
                "https://random.dog/woof.png",
                "https://random.dog/woof.gif"
        );

        String url = dogs.get(random.nextInt(dogs.size()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.fun.dog_title"))
                .setImage(url)
                .setColor(Color.ORANGE)
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }

    private void handleHug(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        Member target = getMemberOption(event, "user");

        if (target == null) {
            event.reply(LangManager.get(lang, "discord.fun.hug_no_target")).queue();
            return;
        }

        String message = LangManager.get(lang, "discord.fun.hug")
                .replace("%user1%", event.getUser().getName())
                .replace("%user2%", target.getEffectiveName());

        event.reply(message).queue();
    }

    private void handleSlap(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        Member target = getMemberOption(event, "user");

        if (target == null) {
            event.reply(LangManager.get(lang, "discord.fun.slap_no_target")).queue();
            return;
        }

        String message = LangManager.get(lang, "discord.fun.slap")
                .replace("%user1%", event.getUser().getName())
                .replace("%user2%", target.getEffectiveName());

        event.reply(message).queue();
    }

    private void handleMeme(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        List<String> memes = List.of(
                "https://i.imgflip.com/30b1gx.jpg",
                "https://i.imgflip.com/4t0m5.jpg",
                "https://i.imgflip.com/1bij.jpg"
        );

        String url = memes.get(random.nextInt(memes.size()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.fun.meme_title"))
                .setImage(url)
                .setColor(Color.YELLOW)
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }

    private void handleGif(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        String query = getStringOption(event, "query");
        if (query == null || query.isEmpty()) {
            event.reply(
                    LangManager.get(lang, "errors.missing_option")
                            .replace("%option%", "query")
            ).setEphemeral(true).queue();
            return;
        }

        query = query.toLowerCase();

        List<String> gifs;
        switch (query) {
            case "cat":
                gifs = List.of(
                        "https://media.giphy.com/media/JIX9t2j0ZTN9S/giphy.gif",
                        "https://media.giphy.com/media/mlvseq9yvZhba/giphy.gif"
                );
                break;
            case "dog":
                gifs = List.of(
                        "https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif",
                        "https://media.giphy.com/media/3o6Zt481isNVuQI1l6/giphy.gif"
                );
                break;
            case "happy":
                gifs = List.of(
                        "https://media.giphy.com/media/111ebonMs90YLu/giphy.gif",
                        "https://media.giphy.com/media/yoJC2GnSClbPOkV0eA/giphy.gif"
                );
                break;
            case "sad":
                gifs = List.of(
                        "https://media.giphy.com/media/d2lcHJTG5Tscg/giphy.gif",
                        "https://media.giphy.com/media/ROF8OQvDmxytW/giphy.gif"
                );
                break;
            default:
                gifs = List.of(
                        "https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif"
                );
                break;
        }

        String url = gifs.get(random.nextInt(gifs.size()));

        event.reply(LangManager.get(lang, "discord.fun.gif_prefix") + " " + url).queue();
    }

    private void handleQuote(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        List<String> quotes = List.of(
                LangManager.get(lang, "discord.fun.quote1"),
                LangManager.get(lang, "discord.fun.quote2"),
                LangManager.get(lang, "discord.fun.quote3")
        );

        String quote = quotes.get(random.nextInt(quotes.size()));

        String message = LangManager.get(lang, "discord.fun.quote")
                .replace("%quote%", quote);

        event.reply(message).queue();
    }

    private void handleShip(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        Member user1 = getMemberOption(event, "user1");
        Member user2 = getMemberOption(event, "user2");

        if (user1 == null || user2 == null) {
            event.reply(LangManager.get(lang, "errors.no_user")).setEphemeral(true).queue();
            return;
        }

        int percent = random.nextInt(101);

        String emoji;
        if (percent > 90) emoji = "💖";
        else if (percent > 70) emoji = "❤️";
        else if (percent > 50) emoji = "💛";
        else if (percent > 30) emoji = "💙";
        else if (percent > 10) emoji = "💔";
        else emoji = "💀";

        String message = LangManager.get(lang, "discord.fun.ship")
                .replace("%user1%", user1.getEffectiveName())
                .replace("%user2%", user2.getEffectiveName())
                .replace("%percent%", String.valueOf(percent))
                .replace("%emoji%", emoji);

        event.reply(message).queue();
    }

    private void handleLoveCalc(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        Member user1 = getMemberOption(event, "user1");
        Member user2 = getMemberOption(event, "user2");

        if (user1 == null || user2 == null) {
            event.reply(LangManager.get(lang, "errors.no_user")).setEphemeral(true).queue();
            return;
        }

        int percent = random.nextInt(101);

        String emoji;
        if (percent > 90) emoji = "💘";
        else if (percent > 70) emoji = "💖";
        else if (percent > 50) emoji = "❤️";
        else if (percent > 30) emoji = "💛";
        else if (percent > 10) emoji = "💙";
        else emoji = "💔";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.fun.lovecalc_title"))
                .setColor(Color.PINK)
                .addField(
                        LangManager.get(lang, "discord.fun.lovecalc_pair_title"),
                        user1.getEffectiveName() + " ❤️ " + user2.getEffectiveName(),
                        false
                )
                .addField(
                        LangManager.get(lang, "discord.fun.lovecalc_result_title"),
                        percent + "% " + emoji,
                        false
                )
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }
}
