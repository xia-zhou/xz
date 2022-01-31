#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.endpoint;

import com.cydeer.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author song.z
 * @date 2022/1/30 9:40 下午
 */
@RestController
public class HealthCheckController {

    @GetMapping({"/", "/health/readiness"})
    public Result<String> health() {
        return new Result<>("success");
    }
}
