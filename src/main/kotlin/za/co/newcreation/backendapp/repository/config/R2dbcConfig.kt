package za.co.newcreation.backendapp.repository.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.DialectResolver
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

@Configuration
class R2dbcConfig {

    @Bean
    fun r2dbcCustomConversions(connectionFactory: ConnectionFactory): R2dbcCustomConversions {
        val dialect = DialectResolver.getDialect(connectionFactory)

        val converters: List<Converter<*, *>> = listOf(
            LocalDateTimeToOffsetDateTimeConverter(),
            OffsetDateTimeToLocalDateTimeConverter()
        )

        return R2dbcCustomConversions.of(dialect, converters)
    }
}

@ReadingConverter
class LocalDateTimeToOffsetDateTimeConverter: Converter<LocalDateTime, OffsetDateTime> {
    override fun convert(source: LocalDateTime): OffsetDateTime? {
        return source.atZone(ZoneId.systemDefault()).toOffsetDateTime()
    }
}

@WritingConverter
class OffsetDateTimeToLocalDateTimeConverter: Converter<OffsetDateTime, LocalDateTime> {
    override fun convert(source: OffsetDateTime): LocalDateTime? {
        return source.toLocalDateTime()
    }
}