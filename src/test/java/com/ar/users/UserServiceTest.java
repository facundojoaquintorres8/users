package com.ar.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.ar.users.config.JwtTokenUtil;
import com.ar.users.dto.PhoneDTO;
import com.ar.users.dto.SignUpRequestDTO;
import com.ar.users.dto.SignUpResponseDTO;
import com.ar.users.dto.UserDTO;
import com.ar.users.exception.CustomException;
import com.ar.users.model.User;
import com.ar.users.repository.IUserRepository;
import com.ar.users.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private JwtTokenUtil jwtTokenUtilMock;

	@Mock
	private IUserRepository repositoryMock;

	@InjectMocks
	private UserService userService;

	@Nested
	@DisplayName("signUpTests")
	class signUpTests {

		@BeforeEach
		void setUp() {
			ReflectionTestUtils.setField(userService, "ivSecretKey", "$ItIsATest#12345");
			ReflectionTestUtils.setField(userService, "secretKey", "$ItIsATest#67890");
		}

		@Test
		@DisplayName("Sign up OK")
		void signUpOkTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setName("Messi");
			request.setEmail("aa@aa.com");
			request.setPassword("aaa12aaaA");
			List<PhoneDTO> phones = new ArrayList<>();
			phones.add(new PhoneDTO());
			request.setPhones(phones);
			Optional<User> optionaUserMock = Optional.empty();
			User userMock = new User();
			userMock.setId(UUID.randomUUID());

			when(repositoryMock.findByEmail(anyString())).thenReturn(optionaUserMock);
			when(jwtTokenUtilMock.getToken(any())).thenReturn("fake_token");
			when(repositoryMock.save(any())).thenReturn(userMock);

			SignUpResponseDTO response = userService.signUp(request);

			assertNotNull(response);
			assertEquals("fake_token", response.getToken());

			verify(repositoryMock, times(1)).findByEmail(anyString());
			verify(repositoryMock, times(1)).save(any());
			verify(jwtTokenUtilMock, times(1)).getToken(any());
		}

		@Test
		@DisplayName("Sign up OK without name")
		void signUpOkWithoutNameTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setEmail("aa@aa.com");
			request.setPassword("aaa12aaaA");
			List<PhoneDTO> phones = new ArrayList<>();
			phones.add(new PhoneDTO());
			request.setPhones(phones);
			Optional<User> optionaUserMock = Optional.empty();
			User userMock = new User();
			userMock.setId(UUID.randomUUID());

			when(repositoryMock.findByEmail(anyString())).thenReturn(optionaUserMock);
			when(jwtTokenUtilMock.getToken(any())).thenReturn("fake_token");
			when(repositoryMock.save(any())).thenReturn(userMock);

			SignUpResponseDTO response = userService.signUp(request);

			assertNotNull(response);
			assertEquals("fake_token", response.getToken());

			verify(repositoryMock, times(1)).findByEmail(anyString());
			verify(repositoryMock, times(1)).save(any());
			verify(jwtTokenUtilMock, times(1)).getToken(any());
		}

		@Test
		@DisplayName("Sign up OK without phones")
		void signUpOkWithoutPhonesTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setName("Messi");
			request.setEmail("aa@aa.com");
			request.setPassword("aaa12aaaA");
			Optional<User> optionaUserMock = Optional.empty();
			User userMock = new User();
			userMock.setId(UUID.randomUUID());

			when(repositoryMock.findByEmail(anyString())).thenReturn(optionaUserMock);
			when(jwtTokenUtilMock.getToken(any())).thenReturn("fake_token");
			when(repositoryMock.save(any())).thenReturn(userMock);

			SignUpResponseDTO response = userService.signUp(request);

			assertNotNull(response);
			assertEquals("fake_token", response.getToken());

			verify(repositoryMock, times(1)).findByEmail(anyString());
			verify(repositoryMock, times(1)).save(any());
			verify(jwtTokenUtilMock, times(1)).getToken(any());
		}

		@Test
		@DisplayName("User exists")
		void userExistsTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setEmail("aa@aa.com");
			request.setPassword("aaa12aaaA");
			Optional<User> optionaUserMock = Optional.of(new User());

			when(repositoryMock.findByEmail(anyString())).thenReturn(optionaUserMock);

			CustomException ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});

			assertNotNull(ex);
			assertEquals("User already exists", ex.getMessage());

			verify(repositoryMock, times(1)).findByEmail(anyString());
			verify(repositoryMock, never()).save(any());
			verify(jwtTokenUtilMock, never()).getToken(any());
		}

		@Test
		@DisplayName("Email not entered")
		void emailNotEnteredTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			CustomException ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertNotNull(ex);
			assertEquals("Email not entered", ex.getMessage());

			request.setEmail("");
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertNotNull(ex);
			assertEquals("Email not entered", ex.getMessage());

			request.setEmail(" ");
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertNotNull(ex);
			assertEquals("Email not entered", ex.getMessage());

			verify(repositoryMock, never()).findByEmail(anyString());
			verify(repositoryMock, never()).save(any());
			verify(jwtTokenUtilMock, never()).getToken(any());
		}

		@Test
		@DisplayName("Validate regular expression (email)")
		void validateRegularExpressionEmailTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setEmail("@aa.com"); // without user
			CustomException ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@"); // without domain and TLD
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@@aa.com"); // more than one @
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa.com"); // without @
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@ss"); // without TLD
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@ss.com."); // invalid termination (.)
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@aa.com_"); // invalid termination (_)
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("a*a@aa.com12"); // invalid termination (number)
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("a*a@aa.com"); // invalid character in user (*)
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@a*a.c"); // invalid character in domain (*)
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@a a.com"); // invalid termination (white space)
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@aa.c"); // short TLD
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			request.setEmail("aa@aa.comco"); // large TLD
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Invalid email format", ex.getMessage());

			verify(repositoryMock, never()).findByEmail(anyString());
			verify(repositoryMock, never()).save(any());
			verify(jwtTokenUtilMock, never()).getToken(any());

		}

		@Test
		@DisplayName("Password not entered")
		void passwordNotEnteredTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setEmail("aa@aa.com");
			CustomException ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Password not entered", ex.getMessage());

			request.setPassword("");
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Password not entered", ex.getMessage());

			request.setPassword(" ");
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals("Password not entered", ex.getMessage());

			verify(repositoryMock, never()).findByEmail(anyString());
			verify(repositoryMock, never()).save(any());
			verify(jwtTokenUtilMock, never()).getToken(any());
		}

		@Test
		@DisplayName("Validate regular expression (email)")
		void validateRegularExpressionPasswordTest() throws Exception {
			SignUpRequestDTO request = new SignUpRequestDTO();
			request.setEmail("aa@aa.com");

			request.setPassword("qwerty12"); // Password without capital letter
			CustomException ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals(
					"The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters",
					ex.getMessage());

			request.setPassword("QWerty12"); // Password with more than one capital letter
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals(
					"The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters",
					ex.getMessage());

			request.setPassword("Qwerty1"); // Password with less than two numbers
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals(
					"The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters",
					ex.getMessage());

			request.setPassword("Qwerty123"); // Password with more than two numbers
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals(
					"The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters",
					ex.getMessage());

			request.setPassword("Qwert12"); // Password with less characters
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals(
					"The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters",
					ex.getMessage());

			request.setPassword("Qwerty12qwerty"); // Password with more characters
			ex = assertThrows(CustomException.class, () -> {
				userService.signUp(request);
			});
			assertEquals(
					"The password must have a capital letter, two numbers and a combination with lowercase letters. With a length between 8 and 12 characters",
					ex.getMessage());

			verify(repositoryMock, never()).findByEmail(anyString());
			verify(repositoryMock, never()).save(any());
			verify(jwtTokenUtilMock, never()).getToken(any());
		}
	}

	@Nested
	@DisplayName("loginTests")
	class loginTests {

		@Test
		@DisplayName("Login OK")
		void loginOkTest() throws Exception {
			Optional<User> userMock = Optional.of(new User());

			when(repositoryMock.findByToken(anyString())).thenReturn(userMock);
			when(jwtTokenUtilMock.getToken(any())).thenReturn("fake_token_2");
			when(repositoryMock.save(any())).thenReturn(userMock.get());

			UserDTO user = userService.login("fake_token");

			assertNotNull(user);
			assertEquals("fake_token_2", user.getToken());

			verify(repositoryMock, times(1)).findByToken(anyString());
			verify(repositoryMock, times(1)).save(any());
			verify(jwtTokenUtilMock, times(1)).getToken(any());
		}

		@Test
		@DisplayName("User not found")
		void userNotFoundTest() throws Exception {
			Optional<User> userMock = Optional.empty();

			when(repositoryMock.findByToken(anyString())).thenReturn(userMock);

			CustomException ex = assertThrows(CustomException.class, () -> {
				userService.login("fake_token");
			});

			assertNotNull(ex);
			assertEquals("User not found", ex.getMessage());

			verify(repositoryMock, times(1)).findByToken(anyString());
			verify(repositoryMock, never()).save(any());
			verify(jwtTokenUtilMock, never()).getToken(any());

		}

	}

}
