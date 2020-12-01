package com.github.zibuyu28.homework112801.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MultiDatasource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    private ArrayList<DataSource> allSlaveDataSources;
    private DataSource masterDataSource;

    public MultiDatasource(DataSource masterDataSource, ArrayList<DataSource> allSlaveDataSources) {
        this.allSlaveDataSources = allSlaveDataSources;
        this.masterDataSource = masterDataSource;
        super.setDefaultTargetDataSource(masterDataSource);
        Map<Object, Object> target = new HashMap<>();
        target.put(DataSourceNames.MASTER, masterDataSource);
        super.setTargetDataSources(target);
        super.afterPropertiesSet();
    }

    public void setDataSource(String dataSource) {
        if(dataSource.startsWith(DataSourceNames.SLAVE)) {
            this.setTargetDataSources(getRandomSlave());
        }
        CONTEXT_HOLDER.set(dataSource);
    }

    private Map<Object, Object> getRandomSlave() {
        Random random = new Random();
        int index = random.nextInt(this.allSlaveDataSources.size());
        Map<Object, Object> target = new HashMap<>();
        DataSource dataSource = this.allSlaveDataSources.get(index);
        target.put(DataSourceNames.SLAVE, dataSource);
        target.put(DataSourceNames.MASTER, masterDataSource);
        return target;
    }

    public String getDataSource() {
        return CONTEXT_HOLDER.get();
    }

    public void removeDataSource() {
        CONTEXT_HOLDER.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }
}
