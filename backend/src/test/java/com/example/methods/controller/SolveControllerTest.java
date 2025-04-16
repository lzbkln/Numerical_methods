package com.example.methods.controller;

import com.example.methods.dto.DtoMethod;
import com.example.methods.dto.DtoProblem;
import com.example.methods.service.MethodService;
import com.example.methods.service.ProblemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("SolveController Tests")
public class SolveControllerTest {

    @Mock
    private MethodService methodService;

    @Mock
    private ProblemService problemService;

    @InjectMocks
    private SolveController solveController;

    private MockMvc mockMvc;

//    @Test
//    @DisplayName("Solve Nonlinear Equation")
//    void testSolveNonlinearEquation() throws Exception {
//        SolveProblemRequest request = SolveProblemRequest.builder()
//                .problemName("Решение нелинейных уравнений")
//                .methodName("Метод Ньютона")
//                .userFunction("Math.exp(-x) - 1.9 + x * x")
//                .a(1.0)
//                .b(2.0)
//                .epsilon(0.0001)
//                .build();
//
//        SolveProblemResponse response = SolveProblemResponse.builder().root(1.27277).build();
//        when(methodService.solveProblem(request)).thenReturn(response);
//
//        mockMvc = MockMvcBuilders.standaloneSetup(solveController).build();
//
//        mockMvc.perform(post("/numerical_methods/solve/nonlinear_equation")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"problemName\":\"Решение нелинейных уравнений\",\"methodName\":\"Метод Ньютона\",\"userFunction\":\"Math.exp(-x) - 1.9 + x * x\",\"a\":1.0,\"b\":2.0,\"epsilon\":0.0001}"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("{\"root\":1.27277}"));
//    }

    @Test
    @DisplayName("Get All Problems")
    void testGetAllProblems() throws Exception {
        List<DtoProblem> problems = Arrays.asList(
                DtoProblem.builder().id("1").name("Problem 1").description("Description 1").build(),
                DtoProblem.builder().id("2").name("Problem 2").description("Description 2").build()
        );
        when(problemService.getAllProblems()).thenReturn(problems);

        mockMvc = MockMvcBuilders.standaloneSetup(solveController).build();

        mockMvc.perform(get("/numerical_methods/problems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":\"1\",\"name\":\"Problem 1\",\"description\":\"Description 1\"}," +
                        "{\"id\":\"2\",\"name\":\"Problem 2\",\"description\":\"Description 2\"}]"));
    }

    @Test
    @DisplayName("Get All Methods By Problem Name")
    void testGetAllMethodsByProblemName() throws Exception {
        List<DtoMethod> methods = Arrays.asList(
                DtoMethod.builder().id("1").problemId("1").name("Method 1").description("Description 1").example("Example 1").build(),
                DtoMethod.builder().id("2").problemId("1").name("Method 2").description("Description 2").example("Example 2").build()
        );
        when(methodService.getAllMethodsByProblemName("Problem 1")).thenReturn(methods);

        mockMvc = MockMvcBuilders.standaloneSetup(solveController).build();

        mockMvc.perform(get("/numerical_methods/problems/Problem 1/methods"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":\"1\",\"problemId\":\"1\",\"name\":\"Method 1\",\"description\":\"Description 1\",\"example\":\"Example 1\"}," +
                        "{\"id\":\"2\",\"problemId\":\"1\",\"name\":\"Method 2\",\"description\":\"Description 2\",\"example\":\"Example 2\"}]"));
    }

    @Test
    @DisplayName("Get All About Method")
    void testGetAllAboutMethod() throws Exception {
        DtoMethod method = DtoMethod.builder().id("1").problemId("1").name("Method 1").description("Description 1").example("Example 1").build();
        when(methodService.getMethodByName("Method 1")).thenReturn(method);

        mockMvc = MockMvcBuilders.standaloneSetup(solveController).build();

        mockMvc.perform(get("/numerical_methods/methods/Method 1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"1\",\"problemId\":\"1\",\"name\":\"Method 1\",\"description\":\"Description 1\",\"example\":\"Example 1\"}"));
    }
}
