package ru.rt.sso.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class Entry {

    @Id
    private long id;
    private long entryTypeId;
    private long userId;
    private String entryValue;
}
