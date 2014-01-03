package com.blogspot.pbetkier.collections.functional;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.blogspot.pbetkier.collections.functional.PersonBuilder.person;
import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IterablesIteratorsTest {

    @Captor
    private ArgumentCaptor<Iterable<String>> captor;

    @Test
    public void shouldCreateTableRowsSwitchingBetweenBackgroundColors() {
        // given
        Person antek = person().name("Antek").build();
        Person basia = person().name("Basia").build();
        Person celina = person().name("Celina").build();
        List<Person> persons = Lists.newArrayList(antek, basia, celina);

        // when
        List<TableRow> rows = new ArrayList<>();
        for (int i = 0; i < persons.size(); ++i) {
            rows.add(new TableRow(persons.get(i).getName()));
            if (i % 2 == 0) {
                rows.get(i).setBackground(Color.BLUE);
            } else {
                rows.get(i).setBackground(Color.GREEN);
            }
        }
//        List<TableRow> rows = Lists.newArrayList();
//        Iterator<Color> backgroundProvider = Iterators.cycle(Color.BLUE, Color.GREEN);
//        for (Person person : persons) {
//            rows.add(new TableRow(person.getName(), backgroundProvider.next()));
//        }

        // then
        assertThat(rows).extracting("text").containsExactly("Antek", "Basia", "Celina");
        assertThat(rows).extracting("background").containsExactly(Color.BLUE, Color.GREEN, Color.BLUE);
    }

    @Test
    public void shouldSendPersonNotificationsInBatches() {
        // given
        List<String> toNotify = Lists.newArrayList("Ania", "Basia", "Celina", "Danusia", "Ewa");
        NotificationService notificationService = mock(NotificationService.class);

        // when
        for (List<String> personsBatch : Iterables.partition(toNotify, 2)) {
            notificationService.notifyPersons(personsBatch);
        }

        // then
        verify(notificationService, times(3)).notifyPersons(captor.capture());
        assertThat(Iterables.concat(captor.getAllValues())).containsExactlyElementsOf(toNotify);
    }

}
