package com.karrel.rotate;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    final SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd(E)");

    private final String startDate = "2017.07.31";
    private final String endDate = "2017.08.31";
    private Calendar mStartCalendar;
    private Calendar mEndCalendar;
    private List<String> mRotaionDate = new ArrayList<>();

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
            if (!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && !isOffDay(sCalendar)) {
                StringBuilder builder = new StringBuilder();

                // 월요일이면 한칸띄움
                if (sCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) builder.append("\n");

                // 날짜 입력
                builder.append(format2.format(sCalendar.getTime()));
                builder.append(" > ");

                // 4명중 한명씩 로테이션 돌림
                for (int i = 0; i < 4; i++) {
                    if (i != 0) builder.append(", ");
                    builder.append(humanList.get(idx % humanList.size()));
                    idx++;
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
        humanList.add("민경환");
        humanList.add("강아연");
        humanList.add("김봄이");
        humanList.add("오진주");

        humanList.add("김창현");
        humanList.add(" 박민");
        humanList.add("이주영");
        humanList.add("이윤희");

        humanList.add("김웅찬");
        humanList.add("박숙희");
        humanList.add("하태석");
        humanList.add("이재용");

        humanList.add("이동원");
        humanList.add(" 김난");
        humanList.add("박광현");
        humanList.add("유현석");

        humanList.add("정지혜");
    }

    private void createOffDay() {
        // 쉬는날추가
        dayOff.add("2017.08.01");
        dayOff.add("2017.08.02");
        dayOff.add("2017.08.03");
        dayOff.add("2017.08.04");
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