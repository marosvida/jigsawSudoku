package gamestudio;

import gamestudio.game.jigsawSudoku.consoleUI.ConsoleUI;
import gamestudio.game.jigsawSudoku.core.Field;
import gamestudio.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
            pattern = "gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        //SpringApplication.run(SpringClient.class, args);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui){
        return args -> ui.run();
    }

    @Bean
    public ConsoleUI consoleUI(Field field){
        return new ConsoleUI(field);
    }

    @Bean
    public Field field(){
        return new Field();
    }

    @Bean
    public ScoreService scoreService(){
        return new ScoreServiceRestClient();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceRestClient();
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceRestClient();
    }

    @Bean
    public LevelService levelService(){
        return new LevelServiceJPA();
    }

    @Bean
    public UserService userService() {
        return new UserServiceJPA();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
