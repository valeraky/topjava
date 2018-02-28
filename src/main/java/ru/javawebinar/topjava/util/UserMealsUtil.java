package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
	public static void main(String[] args) {
		List<UserMeal> mealList = Arrays.asList(
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
				new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
		);
		getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

	}

	public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
		//
		Map<LocalDate, Integer> caloriesByDate = mealList
				.stream()
				.collect(Collectors.groupingBy(UserMeal::getLocalDate,
						Collectors.summingInt(UserMeal::getCalories)));
		//
		return mealList.stream()
				.filter((c) -> TimeUtil.isBetween(c.getLocalTime(), startTime, endTime))
				.map((meal) -> new UserMealWithExceed(meal.getDateTime(),
						meal.getDescription(),
						meal.getCalories(),
						caloriesByDate.get(meal.getLocalDate()) > caloriesPerDay))
				.collect(Collectors.toList());

	}

	public static List<UserMealWithExceed> getFilteredWithExceededViaLoop(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
		Map<LocalDate, Integer> caloriesByDate = new HashMap<>();

		for (UserMeal meal : mealList) {
			caloriesByDate.merge(meal.getLocalDate(), meal.getCalories(), Integer::sum);
		}

		List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();

		for (UserMeal meal : mealList) {
			UserMealWithExceed userMealWithExceed = new UserMealWithExceed(meal.getDateTime(),
					meal.getDescription(),
					meal.getCalories(),
					caloriesByDate.get(meal.getLocalDate()) > caloriesPerDay);
			userMealWithExceedList.add(userMealWithExceed);
		}

		return userMealWithExceedList;
	}

}
