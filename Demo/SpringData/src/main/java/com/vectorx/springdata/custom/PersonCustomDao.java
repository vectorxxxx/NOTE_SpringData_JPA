package com.vectorx.springdata.custom;

import com.vectorx.springdata.entities.Person;

/**
 * PersonDao
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-27 23:21:47
 */
interface PersonCustomDao
{
    Person findById(Integer id);
}
