package com.march.libs.lunar;

/**
 * babyphoto_app     com.babypat.util.lunar
 * Created by 陈栋 on 16/1/25.
 * 功能:
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.Calendar;

public class LunarCalendar {
    private String lunarYear;
    private String lunarMonth;
    private String lunarDate;
    private String lunarAnimal;
    private String errorMessage;

    public LunarCalendar(String year, String month, String date) {
        try {
            int nyear = Integer.parseInt(year);
            int nmonth = Integer.parseInt(month) - 1;
            int ndate = Integer.parseInt(date);
            Calendar e = Calendar.getInstance();
            e.set(nyear, nmonth, ndate);
            Lunar lunar = new Lunar(e);
            this.lunarYear = lunar.cyclical();
            this.lunarMonth = lunar.getCMonth();
            this.lunarDate = lunar.getCDate();
            this.lunarAnimal = lunar.getAnimal();
            this.errorMessage = null;
        } catch (NumberFormatException var9) {
            this.errorMessage = "Year/month/date 至少有一个不是数字!";
            this.lunarYear = this.lunarMonth = this.lunarDate = this.lunarAnimal = null;
        }

    }

    public LunarCalendar(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date);
        Lunar lunar = new Lunar(cal);
        this.lunarYear = lunar.cyclical();
        this.lunarMonth = lunar.getCMonth();
        this.lunarDate = lunar.getCDate();
        this.lunarAnimal = lunar.getAnimal();
        this.errorMessage = null;
    }

    public LunarCalendar(Calendar cal) {
        Lunar lunar = new Lunar(cal);
        this.lunarYear = lunar.cyclical();
        this.lunarMonth = lunar.getCMonth();
        this.lunarDate = lunar.getCDate();
        this.lunarAnimal = lunar.getAnimal();
        this.errorMessage = null;
    }

    public String getLunarYear() {
        return this.lunarYear;
    }

    public String getLunarMonth() {
        return this.lunarMonth;
    }

    public String getLunarDate() {
        return this.lunarDate;
    }

    public String getLunarAnimal() {
        return this.lunarAnimal;
    }

    public String toString() {
        return this.lunarYear + "年" + this.lunarMonth + "月" + this.lunarDate;
    }

    public String getFullInfo() {
        return this.lunarYear + "年" + this.lunarMonth + "月" + this.lunarDate + ",生肖:" + this.lunarAnimal;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
