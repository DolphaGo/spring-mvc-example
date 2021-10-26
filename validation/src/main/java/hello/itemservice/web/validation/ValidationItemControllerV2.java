package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     * @param bindingResult 순서가 중요하다.
     * ModelAttribute Item item 다음에 와야만 한다.(item에 대한 bindResult를 의미함)
     *
     * bindingResult가 있을 때는 일단 컨트롤러가 호출이 된다.
     *
     * @ModelAttribute에 바인딩 시 타입 에러가 발생한다면?(수량에 qqq라는 값이 들어간다거나)
     * bindingResult가 없으면 -> 400오류가 발생하면서 White Label Page로 너어감
     * bindingResult가 있으면 -> 오류정보(FieldError)를 bindResult에 담아서 컨트롤러를 정상 호출한다.
     *
     * BindingResult에 검증 오류를 적용하는 방법
     *
     * 1. @ModelAttribute 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError를 생성해서 BindingResult에 넣어준다.
     * 2. 개발자가 직접 넣어준다.
     * 3. Validator 사용
     *
     * 따라서 bindResult는 검증할 대상 바로 다음에 와야만 한다. 순서가 중요하다.
     * 예를 들어서 @ModelAttribute Item item 에 대해서 item에 대해 검증하고 싶다면 바로 다음에 BindResult가 와야만 한다.
     * BindResult는 Model에 자동으로 포함된다.
     */
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지만 허용합니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() <= 0) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 1개이상, 최대 9,999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            final int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            // bindingResult는 자동으로 view에 담아갑니다. 그래서 따로 modelAttribute로 담지 않아도 됩니다.
            // 스프링에서 BindingResult가 기본적으로 되어있다는 것이 그런 것들을 포함하고 있는 의미이기 때문입니다.
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * rejectedValue로 인해 클라이언트에서 검증에 위반되는 값을 입력해도, 에러 메세지와 동시에 클라이언트 쪽에 값이 지워지지 않는다.
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            // binding 자체는 성공했으므로 false이다. (필드와 맞게 데이터가 안넘어왔냐?는 의미임)
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지만 허용합니다."));
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지만 허용합니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() <= 0) {
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 1개이상, 최대 9,999까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 1개이상, 최대 9,999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            final int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            // bindingResult는 자동으로 view에 담아갑니다. 그래서 따로 modelAttribute로 담지 않아도 됩니다.
            // 스프링에서 BindingResult가 기본적으로 되어있다는 것이 그런 것들을 포함하고 있는 의미이기 때문입니다.
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[] { "required.item.itemName" }, null, "상품 이름은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(
                    new FieldError("item", "price", item.getPrice(), false, new String[] { "range.item.price" }, new Object[] { 1000, 1000000 }, "가격은 1,000 ~ 1,000,000 까지만 허용합니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() <= 0) {
            bindingResult.addError(
                    new FieldError("item", "quantity", item.getQuantity(), false, new String[] { "max.item.quantity" }, new Object[] { 9999 }, "수량은 1개이상, 최대 9,999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            final int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(
                        new ObjectError("item", new String[] { "totalPriceMin", "required.default" }, new Object[] { 10000, resultPrice }, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
                // new String[]인 이유는, 순서대로 우선순위를 갖고 있기 때문입니다.
                // default message 조차 없으면 오류가 나게 됩니다.
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            // bindingResult는 자동으로 view에 담아갑니다. 그래서 따로 modelAttribute로 담지 않아도 됩니다.
            // 스프링에서 BindingResult가 기본적으로 되어있다는 것이 그런 것들을 포함하고 있는 의미이기 때문입니다.
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

