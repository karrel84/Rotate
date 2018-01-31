package com.karrel.rotate;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    private final String startDate = "2018.02.05";
    private final String endDate = "2018.02.31";
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
    public void calClean() {
        Calendar sCalendar = (Calendar) mStartCalendar.clone();
        Calendar eCalendar = (Calendar) mEndCalendar.clone();

        // 1주의 금요일씩 돌린다.

        boolean hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        sCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        StringBuilder builder = new StringBuilder();
        int index = 0;
        while (hasNext) {
            // 쉬는 날이 아니면 로테이션
            if (isOffDay(sCalendar)) {
                // add week
                sCalendar.add(Calendar.WEEK_OF_MONTH, 1);
                // check hasNext
                hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
                continue;
            }

            // 2018.02.09(금)
            String date = format2.format(sCalendar.getTime());
            builder.append("\n");
            builder.append(date);
            // 빗자루 5명, 유리 3명 마대 4명, 나머지 잉여
            builder.append("\n빗자 : ");
            for (int i = 0; i < 5; i++) {
                index = index % humanList.size();
                builder.append(" " + humanList.get(index++));
            }
            builder.append("\n유리 : ");
            for (int i = 0; i < 3; i++) {
                index = index % humanList.size();
                builder.append(" " + humanList.get(index++));
            }
            builder.append("\n마대 : ");
            for (int i = 0; i < 4; i++) {
                index = index % humanList.size();
                builder.append(" " + humanList.get(index++));
            }
            builder.append("\n");

            // add week
            sCalendar.add(Calendar.WEEK_OF_MONTH, 1);
            // check hasNext
            hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        }

        System.out.println(builder.toString());
    }

    @Test
    public void calWorkOut() {
        // 이번주에 몇바퀴 돌지 결정
        // 이번주 로테이션 가능 요일 만들기
        // 한주의 시작은 월요일이 되어야한다.
        Calendar sCalendar = (Calendar) mStartCalendar.clone();
        Calendar eCalendar = (Calendar) mEndCalendar.clone();

        // 로테이션 돌 캘린더
        Calendar weekCalendar;

        StringBuilder builder = new StringBuilder();

        // 이번주 월요일부터 금요일까지 체크
        // 돌아오는주 월요일부터 목요일까지 가능날짜 체크
        boolean hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();
        while (hasNext) {

//            System.out.println(format2.format(sCalendar.getTime()));
            // 이번주 하루에 일퇴가능한 사람수 가져오기
            int dayMan = getDayMan(sCalendar);

            List<List<String>> listList = new ArrayList<>();
            listList.add(new ArrayList<String>()); // monday
            listList.add(new ArrayList<String>()); // tue
            listList.add(new ArrayList<String>()); // wed
            listList.add(new ArrayList<String>()); // thu

            Calendar countCalendar = (Calendar) sCalendar.clone();
            countCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            int index = 0;
            while (index < humanList.size()) {
                // 금요일이면 월요일로 바꾼다.
                if (countCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    countCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                }

//                System.out.println(format2.format(countCalendar.getTime()));

                // 쉬는날이면 빼고
                if (isOffDay(countCalendar)) {
                    // 요일 카운트를 돌리자
                    countCalendar.add(Calendar.DAY_OF_WEEK, -1);
//                    System.out.println("continue !!");
                    continue;
                }

                if (countCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    listList.get(0).add(humanList.get(index));
                } else if (countCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                    listList.get(1).add(humanList.get(index));
                } else if (countCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                    listList.get(2).add(humanList.get(index));
                } else if (countCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                    listList.get(3).add(humanList.get(index));
                }

                // increase index
                ++index;
//                System.out.println("index : " + index);
                // 요일 카운트를 돌리자
                countCalendar.add(Calendar.DAY_OF_WEEK, -1);
            }

            builder.append("\n");
            builder.append("\n");
            countCalendar = (Calendar) sCalendar.clone();
            if (listList.get(0).size() != 0) {
                countCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                builder.append(format2.format(countCalendar.getTime()) + " > ");
                for (String str : listList.get(0)) {
                    builder.append(" " + str);
                }
            }
            builder.append("\n");
            if (listList.get(1).size() != 0) {
                countCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                builder.append(format2.format(countCalendar.getTime()) + " > ");
                for (String str : listList.get(1)) {
                    builder.append(" " + str);
                }
            }
            builder.append("\n");
            if (listList.get(2).size() != 0) {
                countCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                builder.append(format2.format(countCalendar.getTime()) + " > ");
                for (String str : listList.get(2)) {
                    builder.append(" " + str);
                }
            }
            builder.append("\n");
            if (listList.get(3).size() != 0) {
                countCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                builder.append(format2.format(countCalendar.getTime()) + " > ");
                for (String str : listList.get(3)) {
                    builder.append(" " + str);
                }
            }

            // 한주씩 더하자
            sCalendar.add(Calendar.WEEK_OF_MONTH, 1);
            // 루프체크
            hasNext = sCalendar.getTimeInMillis() < eCalendar.getTimeInMillis();

            // 순서변경
            humanList.add(humanList.remove(0));
        }
        System.out.println(builder.toString());
    }


    private int getDayMan(Calendar sCalendar) {
        // 해당 주의 카운팅을하기위해 클론
        Calendar weekCalendar = (Calendar) sCalendar.clone();

        int weekCount = 0;
        // while 문을 돌려서 칼퇴가능 카운트를 가져온다.
        while (weekCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            final int dayOfWeek = weekCalendar.get(Calendar.DAY_OF_WEEK);
            if (!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) && !isOffDay(weekCalendar)) {
                weekCount++;
            }

            // 날짜 카운팅
            weekCalendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        // 하루 일퇴 가능한 사람 수
        int dayMan = (int) Math.ceil((float) humanList.size() / (float) weekCount);
        return dayMan;
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
        humanList.add(getName("김웅찬"));
        humanList.add(getName("오진주"));
        humanList.add(getName("정지혜"));
        humanList.add(getName("박민"));
        humanList.add(getName("김창현"));
        humanList.add(getName("이주영"));
        humanList.add(getName("하태석"));
        humanList.add(getName("이재용"));
        humanList.add(getName("강아연"));
        humanList.add(getName("홍경원"));
        humanList.add(getName("신찬용"));
        humanList.add(getName("정지웅"));
        humanList.add(getName("조광섭"));
        humanList.add(getName("김봄이"));
        humanList.add(getName("김난"));
        humanList.add(getName("이윤희"));
        humanList.add(getName("이근호"));
        humanList.add(getName("조성구"));
        humanList.add(getName("박광현"));
        humanList.add(getName("최승용"));
    }

    private String getName(String name) {
        return String.format("%3s", name);
    }

    private void createOffDay() {
        // 쉬는날추가
        dayOff.add("2018.01.01"); // 설날
        dayOff.add("2018.03.01"); // 설날
        dayOff.add("2018.02.15"); // 설날
        dayOff.add("2018.02.16"); // 설날

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