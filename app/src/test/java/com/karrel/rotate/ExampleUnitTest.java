package com.karrel.rotate;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private List<String> humanList = new ArrayList<>();
    private List<String> dayOff = new ArrayList<>();

    final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    final SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd(E)", Locale.KOREAN);

    private final String startDate = "2017.10.31";
    private final String endDate = "2017.12.1";
    private Calendar mStartCalendar;
    private Calendar mEndCalendar;
    private List<String> mRotaionDate = new ArrayList<>();

    // 하루에 일퇴하는 사람의 수이다
    private final int rotateCount = 5;

    @Before
    public void before() {
        setupCalendar();
        setupHumanList();
        createOffDay();
    }

    @Test
    public void calcurate() {
        Calendar sCalendar = (Calendar) mStartCalendar.clone();
        Calendar eCalendar = (Calendar) mEndCalendar.clone();

        // 루프돌지 결정
        boolean hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        int idx = 0;
        while (hasNext) {
            final int dayOfWeek = sCalendar.get(Calendar.DAY_OF_WEEK);
            if (!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.FRIDAY) && !isOffDay(sCalendar)) {
                StringBuilder builder = new StringBuilder();

                // 월요일이면 한칸띄움
                if (sCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) builder.append("\n");

                // 날짜 입력
                builder.append(format2.format(sCalendar.getTime()));
                builder.append(" > ");

                // 4명중 한명씩 로테이션 돌림
                for (int i = 0; i < rotateCount; i++) {
                    if (i != 0) builder.append(", ");
                    int cnt = idx % humanList.size();
                    builder.append(humanList.get(cnt));
                    idx++;

                    // 마지막까지 다 돌았으면 목록을 로테이트 시킨다.
                    if (cnt + 1 == humanList.size()) {
                        rotateHumanList();
                    }
                }
                // 한줄 추가
                mRotaionDate.add(builder.toString());
            }
            // 날짜 카운트 증가
            sCalendar.add(Calendar.DAY_OF_MONTH, 1);
            // 루프체크
            hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        }

        printList(mRotaionDate);
    }

    private void rotateHumanList() {
        System.out.println(humanList);
        for (int i = 0; i < rotateCount; i++) {
            String s = humanList.remove(0);
            humanList.add(s);
        }
    }

    private boolean isOffDay(Calendar sCalendar) {
        for (String day : dayOff) {
            try {
                if (format.parse(day).getTime() == sCalendar.getTimeInMillis()) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void setupCalendar() {
        try {
            mStartCalendar = Calendar.getInstance();
            mStartCalendar.setTime(format.parse(startDate));
            mEndCalendar = Calendar.getInstance();
            mEndCalendar.setTime(format.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setupHumanList() {
        humanList.add(getName("박숙희"));
        humanList.add(getName("이주영"));
        humanList.add(getName("하태석"));
        humanList.add(getName("이재용"));
        humanList.add(getName("이동원"));

        humanList.add(getName("김난"));
        humanList.add(getName("박광현"));
        humanList.add(getName("유현석"));
        humanList.add(getName("정지혜"));
        humanList.add(getName("신찬용"));

        humanList.add(getName("조성구"));
        humanList.add(getName("민경환"));
        humanList.add(getName("강아연"));
        humanList.add(getName("김봄이"));
        humanList.add(getName("오진주"));

        humanList.add(getName("김창현"));
        humanList.add(getName("박민"));
        humanList.add(getName("이근호"));
        humanList.add(getName("이윤희"));
        humanList.add(getName("김웅찬"));

    }

    private String getName(String name) {
        return String.format("%3s", name);
    }

    private void createOffDay() {
        // 쉬는날추가
        dayOff.add("2017.08.01");
        dayOff.add("2017.08.02");
        dayOff.add("2017.08.03");
        dayOff.add("2017.08.04");
        dayOff.add("2017.08.15"); // 광복절
        dayOff.add("2017.08.25"); // 워크샵
        // 토요일, 일요일 계산
        // 쉬는날 계산
        try {
            addHoliday();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void printList(List<String> list) {
        for (String offDay : list) {
            System.out.println(offDay);
        }
    }

    private void addHoliday() throws ParseException {
        Calendar sCalendar = (Calendar) mStartCalendar.clone();
        Calendar eCalendar = (Calendar) mEndCalendar.clone();

        boolean hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        while (hasNext) {

            final int dayOfWeek = sCalendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                dayOff.add(format.format(sCalendar.getTime()));
            }

            sCalendar.add(Calendar.DAY_OF_MONTH, 1);
            hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        }
    }
}