/*
 * Copyright 2019 Regents of the University of Minnesota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.umn.nlpnewt;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * An interface for a span of text that has been determined to have some specific meaning or
 * function.
 */
public interface Label {

  /**
   * A comparator that looks at the positions of the labels, first by their start index, then by
   * their end index.
   */
  Comparator<Label> POSITION_COMPARATOR = new ByPosition();

  /**
   * A precondition check that checks whether the indices are valid for a label. Can be used by
   * implementing classes for validation of labels.
   *
   * @param startIndex The start index of the label.
   * @param endIndex   The end index of the label.
   */
  static void checkIndexRange(int startIndex, int endIndex) {
    if (endIndex < startIndex) {
      throw new IllegalArgumentException("end index: " + endIndex + " is less than start index: " + startIndex);
    }
    if (startIndex < 0) {
      throw new IllegalArgumentException("start index: " + startIndex + " is less than 0. end index: " + endIndex);
    }
  }

  /**
   * The index of the first character included in the label.
   *
   * @return Integer index.
   */
  int getStartIndex();

  /**
   * Exclusive end index. The index of the first character not included after the label.
   *
   * @return Integer index.
   */
  int getEndIndex();

  /**
   * Returns the text that is covered by the label from {@code string}.
   *
   * @param string The string to retrieve covered text.
   *
   * @return A {@link CharSequence} view of the covered text in the string.
   */
  default @NotNull CharSequence getCoveredText(@NotNull String string) {
    return string.subSequence(getStartIndex(), getEndIndex());
  }

  /**
   * Returns the document text that is covered by the label from {@code document}.
   *
   * @param document The document to retrieve covered text.
   *
   * @return A {@link CharSequence} view of the covered text in the string.
   */
  default @NotNull CharSequence getCoveredText(@NotNull Document document) {
    return getCoveredText(document.getText());
  }

  /**
   * The comparator that does positional comparison, first by start index, then by end index.
   */
  class ByPosition implements Comparator<Label> {
    @Override
    public int compare(Label o1, Label o2) {
      int compare = Integer.compare(o1.getStartIndex(), o2.getStartIndex());
      if (compare != 0) return compare;
      return Integer.compare(o1.getEndIndex(), o2.getEndIndex());
    }
  }
}
