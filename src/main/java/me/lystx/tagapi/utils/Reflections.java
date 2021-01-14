package me.lystx.tagapi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflections {


    public Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendPacket(Player to, Object packet) {
        try {
            Object playerHandle = to.getClass().getMethod("getHandle", new Class[0]).invoke(to, new Object[0]);
            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet to, Object packet) {
        try {
            Object playerHandle = to.getClass().getMethod("getHandle", new Class[0]).invoke(to, new Object[0]);
            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setField(Object change, String name, Object to) throws Exception {
        Field field = change.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(change, to);
        field.setAccessible(false);
    }

    public Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            if (Modifier.isFinal(field.getModifiers()))
                modifiers.set(field, Integer.valueOf(field.getModifiers() & 0xFFFFFFEF));
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public Field modifiers = getField(Field.class, "modifiers");
}
