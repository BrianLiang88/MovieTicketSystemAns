package trainning.film.services;

import trainning.film.domain.Order;
import trainning.film.domain.StatisticData;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 销售统计
 */
public class SaleStatisticsServices implements Observer {
    private static final ExecutorService executorService =  Executors.newSingleThreadExecutor();

    public final Map<Integer, StatisticData> data = new HashMap<Integer,StatisticData>();

    @Override
    public void update(Observable o, Object args) {
        Runnable runnable = ()->{
                Order order  = (Order) args;
                if (data.containsKey(order.getScheduleId())){
                    StatisticData statisticData = data.get(order.getScheduleId());
                    statisticData.setTicketNum(statisticData.getTicketNum()+order.getQuality());
                    statisticData.setAmount(statisticData.getAmount()+order.getPrice()*order.getQuality());
                }else{

                    StatisticData statisticData = new StatisticData();
                    statisticData.setScheduleId(order.getScheduleId());
                    statisticData.setTicketNum(order.getQuality());
                    statisticData.setAmount(order.getPrice()*order.getQuality());
                    data.put(order.getScheduleId(),statisticData);
                }

        };
        executorService.submit(runnable);
    }


}
