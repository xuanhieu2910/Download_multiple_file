package compedia.vn.tickmi.download_multiple_file_tickmi.config;

import com.antkorwin.xsync.XSync;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {

    @Bean
    public XSync<Long> xSync(){
        return new XSync<>();
    }

}
