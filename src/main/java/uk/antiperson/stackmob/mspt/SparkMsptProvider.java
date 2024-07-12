package uk.antiperson.stackmob.mspt;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;

public class SparkMsptProvider extends MsptProvider {

    Spark spark;
    boolean underLoad;

    public SparkMsptProvider(){
        this.spark = SparkProvider.get();
    }

    @Override
    public double getMspt() {
        if(this.spark.mspt() == null){
            return 0;
        }
        return this.spark.mspt().poll(StatisticWindow.MillisPerTick.SECONDS_10).mean();
    }
}
