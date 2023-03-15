package trainning.film.services;

import trainning.film.DataHelper;
import trainning.film.domain.Order;
import trainning.film.domain.Schedule;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryServices implements Observer {

    private static final ExecutorService executorService =  Executors.newSingleThreadExecutor();

    @Override
    public void update(Observable o, Object arg) {
        Runnable runnable = () ->{
                Order order = (Order)arg;
                Schedule schedule = DataHelper.getScheduleById(order.getScheduleId());
                schedule.setQuota(schedule.getQuota()-order.getQuality());
        };
        executorService.submit(runnable);
    }




}
