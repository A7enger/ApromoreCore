/**
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2021 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.dao.jpa.etl;

import org.apromore.config.BaseTestClass;
import org.apromore.dao.DbConnectorRepository;
import org.apromore.dao.model.DbConnectorDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class DbConnectorDaoUnitTest extends BaseTestClass {
    public static final String CONNECTION_ID = "connection_id";

    @Autowired
    DbConnectorRepository dbConnectorRepository;

    @Test
    public void createDbConnectorDao() {
        // Given
        DbConnectorDao dbConnectorDao = new DbConnectorDao(true, CONNECTION_ID, "username", "password", "url", "database_schema", "port");

        // When
        dbConnectorDao = dbConnectorRepository.saveAndFlush(dbConnectorDao);

        // Then
        assertThat(dbConnectorDao.getId()).isNotNull();
        assertThat(dbConnectorDao.getConnectionKey()).isEqualTo(CONNECTION_ID);
    }

    @Test
    public void getDbConnectorDao() {
        // Given
        DbConnectorDao dbConnectorDaoToSave = new DbConnectorDao(true, CONNECTION_ID, "username", "password", "url", "database_schema", "port");
        dbConnectorDaoToSave = dbConnectorRepository.saveAndFlush(dbConnectorDaoToSave);
        Long id = dbConnectorDaoToSave.getId();

        // When
        DbConnectorDao dbConnectorDaoExpected = dbConnectorRepository.findById(id).get();

        // Then
        assertThat(dbConnectorDaoExpected.getConnectionKey()).isEqualTo(dbConnectorDaoToSave.getConnectionKey());
        assertThat(dbConnectorDaoExpected.getDatabaseSchema()).isEqualTo(dbConnectorDaoToSave.getDatabaseSchema());
        assertThat(dbConnectorDaoExpected.getPassword()).isEqualTo(dbConnectorDaoToSave.getPassword());
        assertThat(dbConnectorDaoExpected.getPort()).isEqualTo(dbConnectorDaoToSave.getPort());
        assertThat(dbConnectorDaoExpected.getId()).isEqualTo(dbConnectorDaoToSave.getId());
        assertThat(dbConnectorDaoExpected.isCreated()).isEqualTo(dbConnectorDaoToSave.isCreated());
        assertThat(dbConnectorDaoExpected.getUrl()).isEqualTo(dbConnectorDaoToSave.getUrl());
        assertThat(dbConnectorDaoExpected.getUsername()).isEqualTo(dbConnectorDaoToSave.getUsername());
    }

    @Test
    public void updateDbConnectorDao() {
        // Given
        DbConnectorDao dbConnectorDaoToSave = new DbConnectorDao(true, CONNECTION_ID, "username", "password", "url", "database_schema", "port");
        dbConnectorDaoToSave = dbConnectorRepository.saveAndFlush(dbConnectorDaoToSave);
        Long id = dbConnectorDaoToSave.getId();
        dbConnectorDaoToSave.setPassword("updatedPassword");
        dbConnectorRepository.saveAndFlush(dbConnectorDaoToSave);

        // When
        DbConnectorDao dbConnectorDaoExpected = dbConnectorRepository.findById(id).get();

        // Then
        assertThat(dbConnectorDaoExpected.getPassword()).isEqualTo("updatedPassword");
    }

    @Test(expected= NoSuchElementException.class)
    public void deleteDbConnectorDao() {
        // Given
        DbConnectorDao dbConnectorDaoToSave = new DbConnectorDao(true, CONNECTION_ID, "username", "password", "url", "database_schema", "port");
        dbConnectorDaoToSave = dbConnectorRepository.saveAndFlush(dbConnectorDaoToSave);
        Long id = dbConnectorDaoToSave.getId();
        dbConnectorRepository.deleteById(id);

        // When
        dbConnectorRepository.findById(id).get();

        // Then
        // NoSuchElementException
    }
 }
