package me.skorrloregaming.impl;

import java.util.UUID;

public class Switches {

    public static class SwitchIntBoolean {
        private int arg0;
        private boolean arg1;

        public SwitchIntBoolean(int arg0, boolean arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public int getArg0() {
            return arg0;
        }

        public void setArg0(int arg0) {
            this.arg0 = arg0;
        }

        public boolean getArg1() {
            return arg1;
        }

        public void setArg1(boolean arg1) {
            this.arg1 = arg1;
        }
    }

    public static class SwitchStringMinigame {
        private String arg0;
        private ServerMinigame arg1;

        public SwitchStringMinigame(String arg0, ServerMinigame arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public String getArg0() {
            return arg0;
        }

        public void setArg0(String arg0) {
            this.arg0 = arg0;
        }

        public ServerMinigame getArg1() {
            return arg1;
        }

        public void setArg1(ServerMinigame arg1) {
            this.arg1 = arg1;
        }
    }

    public static class SwitchIntString {
        private int arg0;
        private String arg1;

        public SwitchIntString(int arg0, String arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public int getArg0() {
            return arg0;
        }

        public void setArg0(int arg0) {
            this.arg0 = arg0;
        }

        public String getArg1() {
            return arg1;
        }

        public void setArg1(String arg1) {
            this.arg1 = arg1;
        }
    }

    public static class SwitchDoubleString {
        private double arg0;
        private String arg1;

        public SwitchDoubleString(double arg0, String arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public double getArg0() {
            return arg0;
        }

        public void setArg0(double arg0) {
            this.arg0 = arg0;
        }

        public String getArg1() {
            return arg1;
        }

        public void setArg1(String arg1) {
            this.arg1 = arg1;
        }
    }

    public static class SwitchIntDouble {
        private int arg0;
        private double arg1;

        public SwitchIntDouble(int arg0, double arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public int getArg0() {
            return arg0;
        }

        public void setArg0(int arg0) {
            this.arg0 = arg0;
        }

        public double getArg1() {
            return arg1;
        }

        public void setArg1(double arg1) {
            this.arg1 = arg1;
        }
    }

    public static class SwitchIntInt {
        private int arg0, arg1;

        public SwitchIntInt(int arg0, int arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public int getArg0() {
            return arg0;
        }

        public void setArg0(int arg0) {
            this.arg0 = arg0;
        }

        public int getArg1() {
            return arg1;
        }

        public void setArg1(int arg1) {
            this.arg1 = arg1;
        }
    }

    public static class SwitchUUIDString {
        private UUID arg0;
        private String arg1;

        public SwitchUUIDString(UUID arg0, String arg1) {
            this.setArg0(arg0);
            this.setArg1(arg1);
        }

        public UUID getArg0() {
            return arg0;
        }

        public void setArg0(UUID arg0) {
            this.arg0 = arg0;
        }

        public String getArg1() {
            return arg1;
        }

        public void setArg1(String arg1) {
            this.arg1 = arg1;
        }
    }
}
