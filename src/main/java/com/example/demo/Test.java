package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

class Test {
    public static void main(String[] args) {
        int[] arr = {5, 1, 6, 3, 7, 2};
        int c = arr.length;

        for (int i = 1; i < c; ++i) {
            System.out.println(i);
        }


//        Arrays.stream(arr).forEach(System.out::println);
    }
}