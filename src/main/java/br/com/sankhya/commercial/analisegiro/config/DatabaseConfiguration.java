package br.com.sankhya.commercial.analisegiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {


@Bean
@Primary
public DataSource dataSource()
{
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource("java:/MGEDS");
        return dataSource;
}
}