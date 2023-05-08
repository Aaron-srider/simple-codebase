package fit.wenchao.simplecodebase.rest

import fit.wenchao.simplecodebase.service.SnippetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull

class ExchangeOrderRequest {
    @NotNull
    var snippetAId: Long? = null
    @NotNull
    var snippetBId: Long? = null
}

class ExchangeOrderResponse{
    var orderMap: Map<Long, Int>? = null
}

@RestController
@RequestMapping("/API")
class SnippetController {

    @Autowired
    lateinit var snippetService: SnippetsService

    @PutMapping("/snippet/order/exchange")
    fun exchangeOrder(@RequestBody @Validated exchangeOrderRequest: ExchangeOrderRequest): Any {
        val exchangeOrderResponse = snippetService.exchangeOrder(exchangeOrderRequest)
        return exchangeOrderResponse
    }

}