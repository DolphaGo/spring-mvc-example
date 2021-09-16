package hello.itemservice.web.basic;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        final List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        final Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@RequestParam String itemName,
//                       @RequestParam int price,
//                       @RequestParam Integer quantity,
//                       Model model) {
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);
//
//        itemRepository.save(item);
//
//        model.addAttribute("item", item); // 저장 이후에 상세 화면에서 보여주고 싶을 때
//
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute Item item, Model model) {
//        itemRepository.save(item);
//        model.addAttribute("item", item); // 저장 이후에 상세 화면에서 보여주고 싶을 때
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV3(@ModelAttribute Item item) { // ModelAttribute에 속성명을 적지 않았을 땐 클래스 이름의 첫 글자를 소문자를 바꾼 것을 속성명으로 넣는다.
//        itemRepository.save(item);
//        /**
//         * ModelAttribute는 2개를 같이 처리함
//         * 1. 모델 객체를 만들어줌
//         * 2. view에 사용하기 때문에 데이터를 담아줌 (@ModelAttribute("여기에 입력한 변수명으로 Model 의 attribute name으로 넣어준다.)
//         */
////        model.addAttribute("item", item); // 자동 추가가 되기 때문에 생략이 가능하다.
//        return "basic/item";
//    }

    /**
     * 새로 고침이란, 마지막에 했던 행동을 다시 하는 것. 즉 POST 요청을 다시 보내는 것임
     * 이를 해결하는 가장 간단한 방법은 redirect를 하는 것이다.
     * POST REDIRECT GET => PRG 라고 합니다.
     * 실무에서 많이 사용하는 방법임
     * 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
     * 새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
     * 웹 브라우저는 실제 상품 상세 화면으로 다시 이동한다.
     */
//    @PostMapping("/add")
//    public String addItemV4(Item item) { // ModelAttribute 자체를 생략할 수도 있다. (Item -> item을 model의 속성명으로 사용 model.addAttribute("여기에 item", ...);
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV5(Item item) {
//        itemRepository.save(item);
//        return "redirect:/basic/items/" + item.getId(); // 이건 그냥 숫자라 그냥 넘겼지만, 한글과 같은 경우엔 URL 인코딩을 해야만 한다.
//        // 그런 방법을 정의할 수 있는 RedirectAttribute라는 것이 있다.
//    }

    /**
     * 만약 저장이 되었으면 "저장이 되었습니다."라는 문구를 보여달라는 요구사항이 있다면 어떨까?
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        final Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}"; // redirectAttribute에 값이 들어간 itemId가 {pathVariable} 내에 세팅이 된다. 그리고 남은 파라미터인 status는 파라미터로 넘어가게 된다.
        // result : http://localhost:8080/basic/items/3?status=true
    }

    /**
     * RedirectAttributes를 사용하면 URL인코딩도 해주고, pathVariable, QueryParameter까지 처리해준다.
     */







    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        final Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    /**
     * HTML 폼 전송은 PUT / PATCH를 지원하지 않는다. GET/POST만 사용할 수 있다.
     * PUT/PATCH는 HTTP API 전송시에 사용한다.
     * 스프링에서 HTTP POST 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청상 POST 요청이다.
     *
     */

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; // redirect로 이동시키면 url 자체가 변하게 된다. 여기서 {itemId}는 pathVariable을 그대로 가져온다.
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 100, 10));
        itemRepository.save(new Item("itemB", 200, 20));
    }
}

/**
 * @ModelAttribute 는 Item객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)로 입력해준다.
 *
 * @ModelAttribute 는 모델(Model)dp @ModelAttribute로 지정한 객체를 자동으로 넣어준다.
 * @ModelAttribute("hello") Item item -> 이름을 'hello'로 지정
 * model.addAttribute("hello",item); 모델에 hello 이름으로 저장
 */
