package com.omnizia.scrapinguniverse.service;

import org.springframework.stereotype.Service;

@Service
public class LevenshteinAlgorithmService {
  public int levenshteinDistance(String text1, String text2) {
    // Function to calculate the Levenshtein distance between two strings
    int m = text1.length();
    int n = text2.length();

    // Create a 2D array to store the distances
    int[][] dp = new int[m + 1][n + 1];

    // Initialize the first row and column
    for (int i = 0; i <= m; i++) {
      dp[i][0] = i;
    }
    for (int j = 0; j <= n; j++) {
      dp[0][j] = j;
    }

    // Fill in the array with distances
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
          dp[i][j] = dp[i - 1][j - 1];
        } else {
          dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        }
      }
    }

    // Return the distance between the strings
    return dp[m][n];
  }
}
