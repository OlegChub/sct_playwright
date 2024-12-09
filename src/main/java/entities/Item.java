package entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Item {
    private String itemName;
    private String itemDescription;
    private List<Element> elements;
    private List<String> dimensions;
    private String weight;
    private String height;
    private String width;

    public Item(String itemName, String itemDescription) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.elements = new ArrayList<>();
    }

    public void addElement(Element element) {
        elements.add(element);
    }

}

