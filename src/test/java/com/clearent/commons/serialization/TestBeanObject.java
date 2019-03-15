package com.clearent.commons.serialization;

import java.time.LocalDateTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TestBeanObject {
    
    @XmlTransient
    @JsonIgnore
    private static final Serializer<TestBeanObject> SERIALIZER = new SerializerImpl<TestBeanObject>();
    @XmlTransient
    @JsonIgnore
    private static final Serializer<TestBeanObject> MASKING_SERIALIZER = new MaskingSerializerImpl<TestBeanObject>();
    @XmlTransient
    @JsonIgnore
    private static final Deserializer<TestBeanObject> DESERIALIZER = new DeserializerImpl<TestBeanObject>();
    
    private String type;
    @MaskProperty(enabled=true,maskMethodName="maskAmount")
    @JsonProperty("my-card")
    private String card;
    @JsonProperty("old-style-date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="GMT")
    private Date oldStyleDate;

    private String expDate;
    private String amount;
    @JsonProperty("my-string")
    String myString = "A String value";
    int myPrimitiveInt = 1234;
    Integer myInteger = Integer.valueOf(7890);
    double dbl = 23.67;
    @MaskProperty(enabled=true,maskMethodName="maskThisDouble")
    Double myDouble = 34.67;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    LocalDateTime myDateTime = LocalDateTime.of(2016, 1, 1, 1, 1);
    @JsonProperty("api-key")
    @MaskProperty
    private String apiKey;
    @JsonProperty("my-inner-self")
    TestBeanObject myInnerSelf;
    

    TestBeanObject(){
        
    }
    
    public Double maskThisDouble(){
        return 0.0;
    }
    
    public TestBeanObject getMyInnerSelf() {
        return myInnerSelf;
    }
    
    public void setMyInnerSelf(TestBeanObject myInnerSelf) {
        this.myInnerSelf = myInnerSelf;
    }
    
    public Date getOldStyleDate() {
        return oldStyleDate;
    }
    
    public void setOldStyleDate(Date oldStyleDate) {
        this.oldStyleDate = oldStyleDate;
    }

    public LocalDateTime getMyDateTime() {
        return myDateTime;
    }

    public void setMyDateTime(LocalDateTime myDateTime) {
        this.myDateTime = myDateTime;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public int getMyPrimitiveInt() {
        return myPrimitiveInt;
    }

    public void setMyPrimitiveInt(int myPrimitiveInt) {
        this.myPrimitiveInt = myPrimitiveInt;
    }

    public Integer getMyInteger() {
        return myInteger;
    }

    public void setMyInteger(Integer myInteger) {
        this.myInteger = myInteger;
    }

    public double getDbl() {
        return dbl;
    }

    public void setDbl(double dbl) {
        this.dbl = dbl;
    }

    public Double getMyDouble() {
        return myDouble;
    }

    public void setMyDouble(Double myDouble) {
        this.myDouble = myDouble;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String maskAmount(){
        return "XXXXXXXXXXXX1111";
    }
    
    public static TestBeanObject deserialize(String content, MediaType type) {
        return DESERIALIZER.deserialize(content, type, TestBeanObject.class);
    }

    public String serialize(MediaType mediaType) {
        return SERIALIZER.serialize(this, mediaType);
    }
    
    public String maskingSerialize(MediaType mediaType) {
        return MASKING_SERIALIZER.serialize(this, mediaType);
    }

    @Override
    public String toString() {
        return maskingSerialize(MediaType.APPLICATION_JSON);
    }
}