package com.baekseju.howmuch.pos.service

import com.baekseju.howmuch.pos.dto.MenuDto
import com.baekseju.howmuch.pos.entity.Menu
import com.baekseju.howmuch.pos.repository.MenuRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
@ActiveProfiles("dev")
internal class MenuServiceTest {

    @Autowired
    private lateinit var menuService: MenuService

    @MockBean
    private lateinit var menuRepository: MenuRepository

    val menuList = ArrayList<Menu>()

    private fun <T> any(): T {
        return Mockito.any<T>()
    }

    @BeforeEach
    fun setup(){
        setMenu()
    }

    private fun setMenu(){
        menuList.add(Menu(
            id = 1,
            name = "hamburger",
            price = 5000,
            additionalPrice = 500,
            categoryId = 100,
            stock = 50,
            hidden = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ))
        menuList.add(Menu(
            id = 2,
            name = "cola",
            price = 1500,
            additionalPrice = 0,
            categoryId = 103,
            stock = 999,
            hidden = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = LocalDateTime.now()
        ))
        menuList.add(Menu(
            id = 3,
            name = "wing",
            price = 6000,
            additionalPrice = 2000,
            categoryId = 102,
            stock = 10,
            hidden = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ))
    }

    @Test
    fun getMenus(){
        //given
        given(menuRepository.findAllByHiddenIsFalseAndDeleteAtIsNull()).willReturn(
            menuList.filter { menu -> !menu.hidden && menu.deleteAt == null }
        )

        //when
        val menuDtoList = menuService.getMenuList()

        //then
        assertThat(menuDtoList.size).isEqualTo(1)
        assertThat(menuDtoList[0].name).isEqualTo("hamburger")
    }

    @Test
    fun getExistMenuDetail(){
        //given
        val id = 1
        given(menuRepository.findByIdAndHiddenIsFalseAndDeleteAtIsNull(id)).willReturn(
            Optional.of(menuList.first { menu -> menu.id == id && !menu.hidden && menu.deleteAt == null })
        )

        //when
        val menuDetail = menuService.getMenuDetail(id)

        //then
        assertThat(menuDetail.id).isEqualTo(id)
    }

    @Test
    fun getNotExistMenuDetail(){

    }

    @Test
    fun getSoftDeletedMenuDetail(){

    }

    @Test
    fun getHiddenMenuDetail(){

    }

    @Test
    fun addMenuWithValidData(){
        //given
        val id = 1
        given(menuRepository.save(any())).will {
            val menu: Menu = it.getArgument(0)
            Menu(
                id = id,
                name = menu.name,
                price = menu.price,
                additionalPrice = menu.additionalPrice,
                categoryId = menu.categoryId,
                stock = menu.stock,
                hidden = menu.hidden,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        }
        val menuDtoMock = MenuDto(
            name = "hamburger",
            price = 5000,
            additionalPrice = 500,
            categoryId = 100,
            stock = 50,
            hidden = false
        )

        //when
        val menuDto = menuService.addMenu(menuDtoMock)

        //then
        assertThat(menuDto.id).isEqualTo(id)
    }

    @Test
    fun updateExistMenu(){
        //given
        val id = 1
        given(menuRepository.findById(id)).willReturn(Optional.of(menuList[0]))
        val menuDtoMock = MenuDto(
            name = "hamburger",
            price = 10000,
            additionalPrice = 1000,
            categoryId = 100,
            stock = 500,
            hidden = false
        )

        //when
        val menuDto = menuService.updateMenu(id, menuDtoMock)

        //then
        assertThat(menuDto.price).isEqualTo(10000)
        assertThat(menuDto.additionalPrice).isEqualTo(1000)
        assertThat(menuDto.categoryId).isEqualTo(100)
        assertThat(menuDto.stock).isEqualTo(500)
        assertThat(menuDto.hidden).isFalse
    }

    @Test
    fun updateNotExistMenu(){

    }

    @Test
    fun deleteExistMenu(){
        val id = 1
        given(menuRepository.findById(id)).willReturn(Optional.of(menuList[0]))

        val menuDto = menuService.softDeleteMenu(id)

        assertThat(menuDto.deletedAt).isNotNull
    }

    @Test
    fun deleteNotExistMenu(){

    }
}