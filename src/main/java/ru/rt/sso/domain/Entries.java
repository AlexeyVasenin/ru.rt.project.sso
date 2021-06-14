package ru.rt.sso.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Entries {

    @Id
    private long id;
    private long entryTypeId;
    private long userId;
    private String entryValue;


}
