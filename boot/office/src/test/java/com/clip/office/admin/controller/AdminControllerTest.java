//package com.clip.office.admin.controller;
//
//import com.clip.OfficeApplication;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//@ContextConfiguration(classes = OfficeApplication.class)
//@TestPropertySource(properties = {
//        "spring.datasource.redis.host=localhost",
//        "spring.datasource.redis.port=6379",
//        "spring.security.login.max-fail-count=5",
//        "spring.security.login.block-duration-seconds=3600"
//})
//@SpringBootTest
//@AutoConfigureMockMvc
//class AdminControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @AfterEach
//    void tearDown() {
//        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
//    }
//
//    @DisplayName("잘못된 ID,PW로 로그인을 시도하면 해당 아이디의 실패 횟수가 레디스에 저장된다.")
//    @Test
//    void loginPage() throws Exception {
//        //given
//        String userId = "asdf";
//        String userPassword = "asdf";
//        //when
//        mockMvc.perform(
//                post("/office/admin/login")
//                        .param("username", userId)
//                        .param("password", userPassword)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//        //then
//        String failCnt = stringRedisTemplate.opsForValue().get("login:fail:" + userId);
//        assertThat(failCnt).isEqualTo("1");
//    }
//
//    @DisplayName("100번의 요청이 동시에 들어와도 해당 아이디의 실패 횟수가 누락없이 저당된다.")
//    @Test
//    void loginWithMultipleThreads() throws InterruptedException {
//        // given
//        String userId = "asdf";
//        String userPassword = "asdf";
//        int threadCount = 100;
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//
//        // when
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//
//                    mockMvc.perform(
//                            post("/office/admin/login")
//                                    .param("username", userId)
//                                    .param("password", userPassword)
//                                    .contentType(MediaType.APPLICATION_JSON)
//                    );
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        // then
//        String failCnt = stringRedisTemplate.opsForValue().get("login:fail:" + userId);
//        assertThat(failCnt).isEqualTo("100");
//    }
//}