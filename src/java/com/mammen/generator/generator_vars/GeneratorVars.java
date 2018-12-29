package com.mammen.generator.generator_vars;

import org.w3c.dom.Element;

public interface GeneratorVars
{
    void writeXMLAttributes( Element element );
    void readXMLAttributes( Element element );
    void setDefaultValues();
    void changeUnit( Units oldUnit, Units newUnit );
}
