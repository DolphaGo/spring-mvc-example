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

    @PostMapping("/add")
    public String addItemV4(Item item) { // ModelAttribute 자체를 생략할 수도 있다. (Item -> item을 model의 속성명으로 사용 model.addAttribute("여기에 item", ...);
        itemRepository.save(item);
        return "basic/item";
    }

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
