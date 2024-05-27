package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

public class SocketTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 4059;
        String ip = "188.247.183.127";
        Float mainMeterSerial = readRemove("0001000400010018E6040830303232323232C0018100030100010800FF0200", ip, port);
        System.out.println(mainMeterSerial);
    }

    public static void create(String ip, int port) throws InterruptedException {
//        String mainMeterSerial = sendCommand("000100010001000DC0018100010000600100FF0200", "0908", ip, port);
//        String mainMeterName = sendCommand("000100010001000DC00181000100002A0000FF0200", "0910", ip, port);
        List<String> subMeterSerial = sendCommandList("000100010001000DC0018100010100608009FF0200", "0908", ip, port);
        List<String> subMeterNames = getSubMeterName(subMeterSerial, ip, port);
        System.out.println(subMeterSerial);
//        System.out.println(mainMeterName);
//        System.out.println(mainMeterSerial);
        System.out.println(subMeterNames);
    }

    public static List<String> getSubMeterName(List<String> list, String ip, Integer port) throws InterruptedException {
        List<String> result = new ArrayList<>();
        for (String serial : list) {
            String hex = asciiToHex(serial);
            String name = "";
            String command = "0001000400010018E60408" + hex + "C00181000100002A0000FF0200";
            try {
                name = sendCommand(command, "0910", ip, port);
            } catch (Exception e) {
                Thread.sleep(10000);
                System.out.println("error");
            }
//            System.out.println(name);
            result.add(name);
        }
        return result;
    }

    public static String asciiToHex(String asciiString) {
        StringBuilder hexString = new StringBuilder();
        for (char c : asciiString.toCharArray()) {
            hexString.append(String.format("%02X", (int) c));
        }
        return hexString.toString();
    }


    public static List<String> sendCommandList(String response, String divider, String ip, Integer port) {
        List<String> list = new ArrayList<>();
        try {
            String firstResponse = removeExtraZeros(getServerResponse(response, ip, port));
            String[] a = firstResponse.split(divider);
            for (int i = 1; i < a.length; i++) {
                list.add(hexToAscii(a[i]));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendCommand(String response, String divider, String ip, Integer port) {
        try {
            String firstResponse = removeExtraZeros(getServerResponse(response, ip, port));
            String[] a = firstResponse.split(divider);
            return hexToAscii(a[1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hexToAscii(String response) {
        StringBuilder asciiStringBuilder = new StringBuilder();
        for (int i = 0; i < response.length(); i += 2) {
            String hexByte = response.substring(i, i + 2);
            int decimalValue = Integer.parseInt(hexByte, 16);
            char asciiChar = (char) decimalValue;
            asciiStringBuilder.append(asciiChar);
        }
        return asciiStringBuilder.toString();
    }

    public static String byteToHex(byte b) {
        int unsignedInt = b & 0xFF;
        String hex = Integer.toHexString(unsignedInt).toUpperCase();
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static Float readRemove(String response, String ip, Integer port) throws IOException, InterruptedException {
        DecimalFormat df = new DecimalFormat("#.##");
        String input = getServerResponse(response, ip, port);
        System.out.println(input);
        int index = input.indexOf("0006");
        if (index != -1) {
            String substring = input.substring(index + "0006".length() + 4, index + "0006".length() + 8);
            System.out.println(Float.valueOf(Integer.parseInt(substring, 16)) / 1000);
            return Float.valueOf(df.format(Float.valueOf(Integer.parseInt(substring, 16)) / 1000).replace(",","."));
        } else {
            int index1 = input.indexOf("0005");
            String substring = input.substring(index1 + "0005".length() + 4, index1 + "0005".length() + 8);
            return Float.valueOf(df.format(Float.valueOf(Integer.parseInt(substring, 16)) / 1000).replace(",","."));
        }
    }

    public static String removeExtraZeros(String input) {
        input = input.replaceAll("^0+", "").replaceAll("0+$", "");
        input = input.replaceAll("(\\d)0+(?!\\d)", "$1");
        return input;
    }

    public static String getServerResponse(String hexString, String ip, int port) throws IOException, InterruptedException {
        byte[] message = new byte[100];
        byte[] request = hexStringToByteArray(hexString);
        StringBuilder res = new StringBuilder();

        Socket a = new Socket();
        a.connect(new InetSocketAddress(ip, port), 10000);
        if (a.isConnected()) {
            System.out.println("Nice");
        }
        try {
            DataOutputStream dOut = new DataOutputStream(a.getOutputStream());
            dOut.write(request);
            DataInputStream dataInputStream = new DataInputStream(a.getInputStream());
            dataInputStream.read(message);
            for (byte value : message) {
                String b = byteToHex(value);
                res.append(b);
            }
            a.close();
            return res.toString();
        } catch (Exception e) {
            a.close();
            throw new RuntimeException(e);
        }
    }

    public static String getConnectServerResponse(String hexString, String ip, int port) throws IOException, InterruptedException {
        byte[] message = new byte[100];
        byte[] request = hexStringToByteArray(hexString);
        StringBuilder res = new StringBuilder();

        Socket a = new Socket();
        a.connect(new InetSocketAddress(ip, port), 10000);
        if (a.isConnected()) {
            System.out.println("Nice");
        }
        try {
            DataOutputStream dOut = new DataOutputStream(a.getOutputStream());
            dOut.write(request);
            DataInputStream dataInputStream = new DataInputStream(a.getInputStream());
            dataInputStream.read(message);
            for (byte value : message) {
                String b = byteToHex(value);
                res.append(b);
            }
            a.close();
            return "000" + removeExtraZeros(res.toString());
        } catch (Exception e) {
            a.close();
            throw new RuntimeException(e);
        }
    }
}
