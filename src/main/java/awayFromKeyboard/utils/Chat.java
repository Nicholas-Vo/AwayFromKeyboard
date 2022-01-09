package awayFromKeyboard.utils;

import awayFromKeyboard.AwayFromKeyboard;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Chat {
    public static String aqua = ChatColor.AQUA + "";
    public static String black = ChatColor.BLACK + "";
    public static String blue = ChatColor.BLUE + "";
    public static String red = ChatColor.RED + "";
    public static String yellow = ChatColor.YELLOW + "";
    public static String gold = ChatColor.GOLD + "";
    public static String gray = ChatColor.GRAY + "";
    public static String green = ChatColor.GREEN + "";
    public static String white = ChatColor.WHITE + "";

    public static String darkAqua = ChatColor.DARK_AQUA + "";
    public static String darkBlue = ChatColor.DARK_BLUE + "";
    public static String darkGray = ChatColor.DARK_GRAY + "";
    public static String darkGreen = ChatColor.DARK_GREEN + "";
    public static String darkPurple = ChatColor.DARK_PURPLE + "";
    public static String darkRed = ChatColor.DARK_RED + "";

    public static String italic = ChatColor.ITALIC + "";
    public static String pink = ChatColor.LIGHT_PURPLE + "";
    public static String reset = ChatColor.RESET + "";
    public static String strike = ChatColor.STRIKETHROUGH + "";
    public static String alert = ChatColor.DARK_GRAY + "[!]" + ChatColor.RESET;
    public static String arrow = Chat.gray +"> "+Chat.darkGray;
    public static String title = Chat.green + "AwayFromKeyboard " + Chat.gray + "v" + AwayFromKeyboard.VERSION;

    public static String formatUsername(CommandSender sender, String aString) {
        String name = sender instanceof ConsoleCommandSender ? "The console" : sender.getName();
        return aString.replaceAll("%playername%", name);
    }

}

