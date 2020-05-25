package com.instipod.tests.keycloakauthenticators;

import com.instipod.keycloakauthenticators.utils.IPAddressMatcher;

public class TestIPMatching {
    public static void test() {
        IPAddressMatcher matcher = new IPAddressMatcher("10.0.0.0/8");
        if (matcher.matches("10.0.0.1")) {
            System.out.println("ip matching: test 1: passed");
        } else {
            System.out.println("ip matching: test 1: failed");
        }

        matcher = new IPAddressMatcher("192.168.1.0/24");
        if (!matcher.matches("192.168.2.4")) {
            System.out.println("ip matching: test 2: passed");
        } else {
            System.out.println("ip matching: test 2: failed");
        }
    }
}
