package krystian.devices.rfmgateway;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 4/8/2017 10:01 PM
 */
@Service
public class ChartStorage {
    private ConcurrentHashMap<String, List<Point>> charts;

    public ChartStorage() {
        charts = new ConcurrentHashMap<>();
    }

    public Optional<List<Point>> getChart(String deviceID) {
        return Optional.ofNullable(charts.get(deviceID));
    }

    public void addPoints(String deviceID, List<Point> points) {
        if (points.size() == 0) return;
        if (!charts.containsKey(deviceID))
            charts.put(deviceID, new ArrayList<>());
        List<Point> p = charts.get(deviceID);
        int j = 0;
        if (p.size() == 0) {
            p.add(points.get(0));
            j++;
        }
        for (; j < points.size(); j++) {
            Point po = points.get(j);
            for (int i = 0; i < p.size(); i++) {
                if (po.x < p.get(i).x) {
                    p.add(i, po);
                    break;
                }
                if (po.x == p.get(i).x) {
                    p.get(i).y = po.y;
                    break;
                }
                if (i + 1 == p.size())
                    p.add(p.size(), po);
            }
        }
    }

    public void clearPoints(String deviceID) {
        if (charts.containsKey(deviceID))
            charts.put(deviceID, new ArrayList<>());

    }

    public static class Point {
        public float x;
        public float y;
    }
}
