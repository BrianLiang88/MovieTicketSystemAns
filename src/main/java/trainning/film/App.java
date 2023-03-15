package trainning.film;

import trainning.film.domain.Order;
import trainning.film.domain.StatisticData;
import trainning.film.services.InventoryServices;
import trainning.film.services.SaleStatisticsServices;
import trainning.film.utils.DateUtils;

import java.io.*;
import java.util.*;

import static java.lang.System.exit;

public class App {
    private OrderObservable  orderEvent = new OrderObservable();
    private SaleStatisticsServices saleStatisticsServices = new SaleStatisticsServices();
    private InventoryServices inventoryServices = new InventoryServices();

    App(){

       registerObserver();
    }

    void mainMenu() {
      //  DataInputStream dateInputStream = new DataInputStream(System.in);
        for (; ; ) {
            System.out.println("1. 显示电影场次及余票");
            System.out.println("2. 购票登记");
            System.out.println("3. 显示售票记录");
            System.out.println("4. 显示销售统计");
            System.out.println("5. 保存购票记录");
            System.out.println("6. 导入购票记录");
            System.out.println("7. 退出");
            System.out.printf("请选择菜单(1-7)：");
            int result = readInt(1, 7);
            if (result ==-2) {
                //ctrl+d
                exit(1);
            }else if (result == -1) {
                continue;
            }
            switch (result) {
                case 1:
                    showSchedule();
                    break;
                case 2:
                    if (bookOrder() == -2){
                        exit(1);
                    }
                    break;
                case 3:
                    showOrder();
                    break;
                case 4:
                    showStatistics();
                    break;
                case 5:
                    persistOrder();
                    break;
                case 6:
                    loadOrder();
                    break;
                case 7:
                    System.out.println("bye-bye");
                    exit(0);

            }
        }


    }



    void registerObserver(){
        orderEvent.addObserver(inventoryServices);
        orderEvent.addObserver(saleStatisticsServices);
    }

    public static void main(String[] args) {
        DataHelper.initEnv();

        App app = new App();
        app.mainMenu();

    }


    private void showSchedule() {
        System.out.println("--------------可售电影场次安排如下---------------------------");
        DataHelper.scheduleList.forEach(o -> {
            System.out.printf("场次:%d\n", o.getId());
            System.out.printf("电影：%s\n", DataHelper.getFilmById(o.getfId()).getName());
            System.out.printf("放映时间：%s\n", DateUtils.fmtYmdHms(o.getShowTime()));
            System.out.printf("余票数量:%d\n", o.getQuota());

            System.out.println();
        });
        System.out.println("-----------------------------------------");
    }


    private int bookOrder() {
        for(;;) {
            System.out.println("请输入订票信息(0 返回):");
            Integer scheduleId = readScheduleId();
            if (scheduleId < 1) {
                return scheduleId;
            }

            Integer customerId = readCustomerId();
            if (customerId < 1){
                return customerId;
            }

            Integer ticketNum = readTicketNum(scheduleId);
            if (ticketNum < 1) {
                return ticketNum;
            }

            Order order = new Order();
            order.setId(genOrderId(10));
            order.setCustomId(customerId);
            order.setQuality(ticketNum);
            order.setPrice(DataHelper.getScheduleById(scheduleId).getPrice());
            order.setScheduleId(scheduleId);
            order.setStatus("new");
            order.setOrderTime(new Date(System.currentTimeMillis()));

            DataHelper.orderList.add(order);

            //通知统计和库存更新
            orderEvent.notifyObservers(order);


            return 1;
        }

    }

    private void showOrder() {
        if (DataHelper.orderList.size() == 0) {
            System.out.println("暂无订单");
            return;
        }
        System.out.println("---------------------    订单列表如下    ----------------------------------");
        DataHelper.orderList.forEach(o -> {
            System.out.printf("订单号：%s\n", o.getId());
            System.out.printf("电影：%s\n", DataHelper.getFilmById(DataHelper.getScheduleById(o.getScheduleId()).getfId()).getName());
            System.out.printf("场次：%d\n", DataHelper.getScheduleById(o.getScheduleId()).getId());
            System.out.printf("购票人：%s\n", DataHelper.getCustomById(o.getCustomId()).getAlias());
            System.out.printf("购票张数：%d\n", o.getQuality());
            System.out.printf("购票时间：%s\n", DateUtils.fmtYmdHms(o.getOrderTime()));
            System.out.println();
        });
        System.out.println("---------------------------------------------------------------------");
    }


    private void showStatistics() {
            if (saleStatisticsServices.data.size()<1){
                System.out.println("暂无销售记录");
                return;
            }
            System.out.println(" ---------------     分场次统计数据如下  -----------------------------");
            for(Map.Entry<Integer, StatisticData> entry: saleStatisticsServices.data.entrySet()){
                StatisticData value = entry.getValue();
                System.out.printf("场次：%d,票数:%d,金额:%d \n",value.getScheduleId(),value.getTicketNum(),value.getAmount());
            }
            System.out.println(" -----------------------------------------------------------------");

    }

    private void persistOrder() {
        if (DataHelper.orderList.size()<1){
            System.out.println("无订单可以保存");
            return;
        }
        try(
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("order.data")));
        ){
            out.writeObject(DataHelper.orderList);
            out.flush();
            System.out.println(" 订单成功保存到磁盘文件中");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("订单保存到数据文件失败");
        }

    }

    private void loadOrder() {
        File dataFile = new File("order.data");
        if (!dataFile.exists()){
            System.out.println("订单数据文件不存在");
            return;
        }
        try(
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile));
        ) {
            List<Order> orderList = (List) in.readObject();
            if (orderList==null || orderList.size()<1){
                System.out.println("订单数据为空");
                return;
            }
            for(Order order:orderList){
                 orderEvent.notifyObservers(order);
            }
            DataHelper.orderList = orderList;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("订单数据文件加载失败");
        }

    }


    public static int readInt(int min, int max) {
        Scanner in = new Scanner(System.in);
        try {
                int enter = in.nextInt();
                if (enter < min || enter > max) {
                    return -1;
                }else{
                    return enter;
                }
        } catch (InputMismatchException e) {
                //非数字型
                return -1;
        } catch(NoSuchElementException e){
                //ctrl-d
                return -2;

        }
    }


    private static int readScheduleId() {
        System.out.printf("请输入放映场次:");
        int scheduleId = readInt(0, Integer.MAX_VALUE);
        if (scheduleId == -1) {
            return readScheduleId();
        }else if (scheduleId ==-2){
            return -2;
        } else if (scheduleId == 0) {
            return scheduleId;
        } else {
            if (!DataHelper.scheduleMap.containsKey(scheduleId)) {
                System.out.println("场次不存在");
                return readScheduleId();
            } else {
                return scheduleId;
            }
        }
    }


    private static int readCustomerId() {
        System.out.printf("请输入用户ID:");
        int customerId = readInt(0, Integer.MAX_VALUE);
        if (customerId == -1) {
            return readCustomerId();
        } else if (customerId==-2){
            return -2;
        }else if (customerId == 0) {
            return customerId;
        } else {
            if (!DataHelper.customerMap.containsKey(customerId)) {
                System.out.println("用户不存在");
                return readCustomerId();
            } else {
                return customerId;
            }
        }
    }


    private static int readTicketNum(Integer scheduleId) {
        System.out.printf("请输入订购票数:");
        int ticketNum = readInt(0, 200);
        if (ticketNum == -1) {
            return readTicketNum(scheduleId);
        } else if (ticketNum==-2){
            return -2;
        }else if (ticketNum == 0) {
            return ticketNum;
        } else {
            if (DataHelper.scheduleMap.get(scheduleId).getQuota()<ticketNum) {
                System.out.println("余票不足!");
                return readTicketNum(scheduleId);
            } else {
                return ticketNum;
            }
        }
    }

    private static final Random random  = new Random();
    private static String genOrderId(int length){
        StringBuffer result = new StringBuffer();
        for(int i=0;i<length;i++) {
            result.append(random.nextInt(9));
        }
        return result.toString();
    }



    class OrderObservable extends  Observable {


        public void notifyObservers(Order order){
            super.setChanged();
            super.notifyObservers(order);
        }
    }
}
