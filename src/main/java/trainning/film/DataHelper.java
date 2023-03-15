package trainning.film;

import trainning.film.domain.Customer;
import trainning.film.domain.Film;
import trainning.film.domain.Order;
import trainning.film.domain.Schedule;
import trainning.film.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {

    static final List<Film> films = new ArrayList<Film>();

    static final Map<Integer, Film> filmMap = new HashMap<Integer, Film>();


    static final List<Schedule> scheduleList = new ArrayList<Schedule>();

    static final Map<Integer, Schedule> scheduleMap = new HashMap<Integer, Schedule>();



    static final List<Customer> customerList = new ArrayList<Customer>();

    static final Map<Integer, Customer> customerMap = new HashMap<Integer, Customer>();


    static  List<Order>  orderList = new ArrayList<Order>();


    public static  void initEnv(){
        initFilm();

        initSchedule();

        initCustom();
    }

    private static void initFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("蜀山传");
        film.setClassify("玄幻");
        film.setDirector("徐克");
        film.setHero("郑伊健");
        film.setHeroine("张伯芝");
        film.setProduction(DateUtils.getDateByYMD("1993-04-23"));
        films.add(film);

        filmMap.put(film.getId(), film);

        Film film2 = new Film();
        film2.setId(2);
        film2.setName("英雄");
        film2.setClassify("武侠");
        film2.setDirector("张艺谋");
        film2.setHero("李连杰");
        film2.setHeroine("章子怡");
        film2.setProduction(DateUtils.getDateByYMD("2002-12-24"));
        films.add(film2);
        filmMap.put(film2.getId(), film2);

        Film film3 = new Film();
        film3.setId(3);
        film3.setName("机械师2：复活");
        film3.setClassify("科幻");
        film3.setDirector("丹尼斯");
        film3.setHero("杰森·斯");
        film3.setHeroine("杰西卡");
        film3.setProduction(DateUtils.getDateByYMD("2016-08-23"));
        films.add(film3);

        filmMap.put(film3.getId(), film3);

    }


    private static void initSchedule() {
        Schedule schedule = new Schedule();
        schedule.setId(100);
        schedule.setfId(1);
        schedule.setPrice(100);
        schedule.setQuota(200);
        schedule.setTheater("New York");
        schedule.setShowTime(DateUtils.getDateByYMDHMS("2022-10-12 09:10:00"));

        scheduleList.add(schedule);
        scheduleMap.put(schedule.getId(), schedule);


        Schedule schedule2 = new Schedule();
        schedule2.setId(120);
        schedule2.setfId(1);
        schedule2.setPrice(100);
        schedule2.setQuota(200);
        schedule2.setTheater("New York");
        schedule2.setShowTime(DateUtils.getDateByYMDHMS("2022-10-12 11:10:00"));

        scheduleList.add(schedule2);
        scheduleMap.put(schedule2.getId(), schedule2);


        Schedule schedule3 = new Schedule();
        schedule3.setId(200);
        schedule3.setfId(2);
        schedule3.setPrice(120);
        schedule3.setQuota(200);
        schedule3.setTheater("华盛顿");
        schedule3.setShowTime(DateUtils.getDateByYMDHMS("2022-10-12 09:10:00"));

        scheduleList.add(schedule3);
        scheduleMap.put(schedule3.getId(), schedule3);


        Schedule schedule4 = new Schedule();
        schedule4.setId(300);
        schedule4.setfId(3);
        schedule4.setPrice(300);
        schedule4.setQuota(150);
        schedule4.setTheater("Boston");
        schedule4.setShowTime(DateUtils.getDateByYMDHMS("2022-10-12 11:10:00"));


        scheduleList.add(schedule4);
        scheduleMap.put(schedule4.getId(), schedule4);

    }

    private static void  initCustom() {

        Customer customer = new Customer();
        customer.setId(11);
        customer.setAlias("JamesWang");

        customerList.add(customer);
        customerMap.put(customer.getId(),customer);



        Customer customer2 = new Customer();
        customer2.setId(32);
        customer2.setAlias("Kathy");

        customerList.add(customer2);
        customerMap.put(customer2.getId(),customer2);


    }


    public static  Film getFilmById(Integer id){
        return filmMap.get(id);
    }

    public static  Customer getCustomById(Integer id){
        return  customerMap.get(id);
    }


    public static Schedule getScheduleById(Integer id){
        return scheduleMap.get(id);

    }
}
