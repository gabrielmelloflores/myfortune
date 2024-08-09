package com.gabrielflores.myfortune.model.entity;

import java.util.Map;

/** 
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 * @Date: 2024-08-07 09:52:41  
*/

public interface StringEnum {

  String name();

  String getValor();

  String getDescricao();

  default Map<String, String> toMap() {
    return Map.of("name", name(), "valor", getValor(), "descricao", getDescricao());
  }
}
