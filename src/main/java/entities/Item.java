package entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Item {
    private String itemName;
    private String itemDescription;
    private List<Element> elements;
    private int height;
    private int width;
    private int length;
    private double weight;
    private int assemblyTime;

    public Item(String itemName, String itemDescription) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.elements = new ArrayList<>();
    }

    public void addElement(Element element) {
        elements.add(element);
    }
}

