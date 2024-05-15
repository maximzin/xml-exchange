package ru.work.xmlexchange.models;

import lombok.Getter;
import lombok.Setter;

public class XmlRow {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String xml;

    public XmlRow(int id, String xml) {
        this.id = id;
        this.xml = xml;
    }
}
