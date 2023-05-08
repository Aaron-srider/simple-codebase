package fit.wenchao.simplecodebase.rest

import fit.wenchao.simplecodebase.model.JsonResult
import fit.wenchao.simplecodebase.service.SnippetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

class UpdateSnippetRequest {
    var lang: String? = null
}

class ExchangeOrderRequest {
    @NotNull
    var snippetAId: Long? = null

    @NotNull
    var snippetBId: Long? = null
}

class ExchangeOrderResponse {
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

    @PutMapping("/snippet/{snippetId}")
    fun update(@PathVariable snippetId: Long, @RequestBody @Validated updateSnippetRequest: UpdateSnippetRequest): Any {
        snippetService.update(snippetId, updateSnippetRequest)
        return JsonResult.ok()
    }

}