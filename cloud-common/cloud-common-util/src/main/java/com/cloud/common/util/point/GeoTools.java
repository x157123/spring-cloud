package com.cloud.common.util.point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GeoTools {
//
//    public static void main(String[] args) {
//        LocationReduce pA = new LocationReduce("104.072348", "30.664513");
//        LocationReduce pB = new LocationReduce("104.078771", "30.669079");
//        LocationReduce pC = new LocationReduce("104.076866", "30.653826");
//        LocationReduce pD = new LocationReduce("104.052648", "30.658238");
//        LocationReduce pE = new LocationReduce("104.044096", "30.678677");
//
//        System.out.println(System.currentTimeMillis());
//
//        System.out.println(geoDist(pA, pB));  //获取
//        System.out.println(getDirection(pA, pB));
//        System.out.println(getDirection(pA, pC));
//        System.out.println(getDirection(pA, pD));
//        System.out.println(getDirection(pA, pE));
//        List<LocationReduce> list = getENPointFromFile("C:\\Users\\liulei\\Desktop\\poit.txt");
//        ArrayList<LocationReduce> newList = new ArrayList<>();
//        Double length = getList(list, newList);
//
//
//        StringJoiner joiner = new StringJoiner(",");
//        list.forEach(item -> joiner.add(item.toString()));
//        System.out.println(joiner.toString());
//        System.out.println("");
//        System.out.println("------------------------------------------------------------------------------------------------");
//        StringJoiner joiners = new StringJoiner(",");
//        newList.forEach(item -> joiners.add(item.toString()));
//        System.out.println(joiners.toString());
//        System.out.println("");
//        //------------------------------------------------------------------------------------------------------------//
//    }

    /**
     * @param pA 点位1
     * @param pB 经度2
     * @return 方向
     */
    public static String getDirection(LocationReduce pA, LocationReduce pB) {
        double direction = getAngle(pA, pB);
        if ((direction <= 10) || (direction > 350))
            return "正东方";
        if ((direction > 10) && (direction <= 80))
            return "东北方";
        if ((direction > 80) && (direction <= 100))
            return "正北方";
        if ((direction > 100) && (direction <= 170))
            return "西北方";
        if ((direction > 170) && (direction <= 190))
            return "正西方";
        if ((direction > 190) && (direction <= 260))
            return "西南方";
        if ((direction > 260) && (direction <= 280))
            return "正南方";
        if ((direction > 280) && (direction <= 350))
            return "东南方";
        return "";
    }

    /**
     * 获取角度
     *
     * @param pA
     * @param pB
     * @return
     */
    public static double getAngle(LocationReduce pA, LocationReduce pB) {
        double x1 = Double.parseDouble(pA.getLon());
        double y1 = Double.parseDouble(pA.getLat());
        double x2 = Double.parseDouble(pB.getLon());
        double y2 = Double.parseDouble(pB.getLat());
        double pi = Math.PI;
        double w1 = y1 / 180 * pi;
        double j1 = x1 / 180 * pi;
        double w2 = y2 / 180 * pi;
        double j2 = x2 / 180 * pi;
        double ret;
        if (j1 == j2) {
            if (w1 > w2) {
                return 270;
            } else if (w1 < w2) {
                return 90;
            } else {
                return -1;
            }
        }
        ret = 4 * Math.pow(Math.sin((w1 - w2) / 2), 2)
                - Math.pow(Math.sin((j1 - j2) / 2) * (Math.cos(w1) - Math.cos(w2)), 2);
        ret = Math.sqrt(ret);
        double temp = (Math.sin(Math.abs(j1 - j2) / 2) * (Math.cos(w1) + Math.cos(w2)));
        ret = ret / temp;
        ret = Math.atan(ret) / pi * 180;
        if (j1 > j2) {
            if (w1 > w2) {
                ret += 180;
            } else {
                ret = 180 - ret;
            }
        } else if (w1 > w2) {
            ret = 360 - ret;
        }
        return ret;
    }


    /**
     * 获取压缩后的经纬度
     *
     * @param locationReduce 压缩器经纬度
     * @return 压缩后的经纬度
     */
    public static Double getList(List<LocationReduce> locationReduce, List<LocationReduce> newList) {
        ArrayList<LocationReduce> pGPSArrayFilter = new ArrayList<>();
        double length = 0;
        double DMax = 30.0;
        pGPSArrayFilter.add(locationReduce.get(0));
        pGPSArrayFilter.add(locationReduce.get(locationReduce.size() - 1));
        LocationReduce[] enpInit = new LocationReduce[locationReduce.size()];
        Iterator<LocationReduce> iInit = locationReduce.iterator();
        int jj = 0;
        while (iInit.hasNext()) {
            enpInit[jj] = iInit.next();
            jj++;
        }
        int start = 0;
        int end = locationReduce.size() - 1;
        compression(enpInit, pGPSArrayFilter, start, end, DMax);
        LocationReduce[] enpFilter = new LocationReduce[pGPSArrayFilter.size()];
        Iterator<LocationReduce> iF = pGPSArrayFilter.iterator();
        int i = 0;
        while (iF.hasNext()) {
            enpFilter[i] = iF.next();
            i++;
        }
        Arrays.sort(enpFilter);
        for (int k = 0; k < enpFilter.length - 1; k++) {
            //设置方向
            enpFilter[k + 1].setDirection(getDirection(enpFilter[k], enpFilter[k + 1]));
            //设置距离
            enpFilter[k + 1].setDistance(geoDist(enpFilter[k], enpFilter[k + 1]));
            //设置速度
            enpFilter[k + 1].setSpeed(getSpeed(enpFilter[k + 1].getDistance(), enpFilter[k + 1].getTime() - enpFilter[k].getTime()));
            //设置总里程数
            length += enpFilter[k + 1].getDistance();
        }
        for (int j = 0; j < enpFilter.length; j++) {
            newList.add(enpFilter[j]);
        }
        return length;
    }

    /**
     * 函数功能：求两个经纬度点之间的距离
     *
     * @param pA：起始点
     * @param pB：结束点
     * @return distance：距离
     */
    public static double geoDist(LocationReduce pA, LocationReduce pB) {
        double radLat1 = Rad(Double.parseDouble(pA.getLat()));
        double radLat2 = Rad(Double.parseDouble(pB.getLat()));
        double delta_lon = Rad(Double.parseDouble(pB.getLon()) - Double.parseDouble(pA.getLon()));
        double top_1 = Math.cos(radLat2) * Math.sin(delta_lon);
        double top_2 = Math.cos(radLat1) * Math.sin(radLat2) - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
        double top = Math.sqrt(top_1 * top_1 + top_2 * top_2);
        double bottom = Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
        double delta_sigma = Math.atan2(top, bottom);
        double distance = delta_sigma * 6378137.0;
        return distance;
    }

    /**
     * 函数功能：使用三角形面积（使用海伦公式求得）
     *
     * @param pA：起始点
     * @param pB：结束点
     * @param pX：第三个点
     * @return distance：点pX到pA和pB所在直线的距离
     */
    private static double distToSegment(LocationReduce pA, LocationReduce pB, LocationReduce pX) {
        double a = Math.abs(geoDist(pA, pB));
        double b = Math.abs(geoDist(pA, pX));
        double c = Math.abs(geoDist(pB, pX));
        double p = (a + b + c) / 2.0;
        double s = Math.sqrt(Math.abs(p * (p - a) * (p - b) * (p - c)));
        double d = s * 2.0 / a;
        return d;
    }

    /**
     * 函数功能：角度转弧度
     *
     * @param d：角度
     * @return 返回的是弧度
     */
    private static double Rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 函数功能：根据最大距离限制，采用DP方法递归的对原始轨迹进行采样，得到压缩后的轨迹
     *
     * @param locationArray：原始经纬度坐标点数组
     * @param arrayList：保持过滤后的点坐标数组
     * @param start：起始下标
     * @param end：终点下标
     * @param max：最大距离误差
     */
    private static void compression(LocationReduce[] locationArray, ArrayList<LocationReduce> arrayList,
                                    int start, int end, double max) {
        if (start < end) {
            double maxDist = 0;
            int index = 0;
            for (int i = start + 1; i < end; i++) {
                double curDist = distToSegment(locationArray[start], locationArray[end], locationArray[i]);
                if (curDist > maxDist) {
                    maxDist = curDist;
                    index = i;
                }
            }
            if (maxDist >= max) {
                arrayList.add(locationArray[index]);
                compression(locationArray, arrayList, start, index, max);
                compression(locationArray, arrayList, index, end, max);
            }
        }
    }

    private static List<LocationReduce> getENPointFromFile(String file) {
        try {
            File fGPS = new File(file);
            ArrayList<LocationReduce> pGPSArray = new ArrayList<LocationReduce>();
            if (fGPS.exists() && fGPS.isFile()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(fGPS));
                BufferedReader bReader = new BufferedReader(read);
                String str;
                String[] strGPS;
                Random ran = new Random();
                long i = 1669886745066L;
                while ((str = bReader.readLine()) != null) {
                    strGPS = str.split(",");
                    LocationReduce p = new LocationReduce();
                    p.setLat(strGPS[0]);
                    p.setLon(strGPS[1]);
                    p.setTime(i += ran.nextInt(7000));
                    pGPSArray.add(p);
                }
                bReader.close();
            }
            return pGPSArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 计算速度 m/s
     *
     * @param distance
     * @param time
     * @return
     */
    private static double getSpeed(Double distance, Long time) {
        double speed = 0;
        if (time != null && time > 0) {
            return distance / (time / 1000);
        }
        return speed;
    }
}