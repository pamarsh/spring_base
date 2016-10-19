package utils.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionMatcher extends TypeSafeDiagnosingMatcher {
    private final Object expectedVo;
    private final Set<String> fieldNames;
    private final List<FieldMatcher> fieldMatchers;

    public ReflectionMatcher(final Object expectedVo) {
        Field[] fieldsToMatch = expectedVo.getClass().getDeclaredFields();
        this.expectedVo = expectedVo;
        this.fieldNames = fieldNamesFrom(fieldsToMatch);
        this.fieldMatchers = fieldMatchersFor(expectedVo, fieldsToMatch);
    }

    @Override
    protected boolean matchesSafely(final Object item, final Description mismatchDescription) {
        return hasAllFields(item, mismatchDescription) && hasMatchingValues(item, mismatchDescription);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("same field values as " + expectedVo.getClass().getSimpleName())
                .appendList(" <", ", ", ">", fieldMatchers);
    }

    private boolean hasMatchingValues(final Object item, final Description mismatchDescription) {
        mismatchDescription.appendText(item + " has <");
        int mismatchCount = 0;

        for (FieldMatcher fieldMatcher : fieldMatchers) {
            if (!fieldMatcher.matches(item)) {
                if (mismatchCount != 0) {
                    mismatchDescription.appendText(", ");
                }
                fieldMatcher.describeMismatch(item, mismatchDescription);
                mismatchCount++;
            }
        }

        mismatchDescription.appendText(">");
        return mismatchCount == 0;
    }

    private boolean hasAllFields(final Object item, final Description mismatchDescription) {
        final Field[] fields = item.getClass().getDeclaredFields();

        final Set<String> itemsFieldNames = fieldNamesFrom(fields);

        boolean result = true;

        for (String fieldName : fieldNames) {
            if (!itemsFieldNames.contains(fieldName)) {
                result = false;
                mismatchDescription.appendText(" missing field: " + fieldName);
            }
        }

        return result;
    }

    private List<FieldMatcher> fieldMatchersFor(final Object expectedVo, final Field[] fields) {
        List<FieldMatcher> result = new ArrayList<>(fields.length);
        try {
            for (Field field : fields) {
                result.add(new FieldMatcher(field, expectedVo));
            }
        }
        catch (NoSuchFieldException e) {
            throw new IllegalStateException("Programmer exception, pls replace programmer: " +
                    "field list doesn't match with the fields of the provided expectedVo", e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("Programmer exception, pls replace programmer: " +
                    "field list doesn't match with the fields of the provided expectedVo", e);
        }
        return result;
    }

    private Set<String> fieldNamesFrom(final Field[] fieldsToMatch) {
        HashSet<String> result = new HashSet<String>();
        for (Field field : fieldsToMatch) {
            result.add(field.getName());
        }
        return result;
    }

    public class FieldMatcher extends DiagnosingMatcher<Object> {

        private final Object expectedFieldValue;
        private final String fieldName;

        private FieldMatcher(Field field, Object expectedVo) throws NoSuchFieldException, IllegalAccessException {
            this.fieldName = field.getName();
            final Field expectedField = expectedVo.getClass().getDeclaredField(fieldName);
            boolean oldAccessable = expectedField.isAccessible();

            expectedField.setAccessible(true);
            this.expectedFieldValue = expectedField.get(expectedVo);
            expectedField.setAccessible(oldAccessable);
        }

        @Override
        protected boolean matches(final Object item, final Description mismatchDescription) {
            try {
                final Field fieldItem = item.getClass().getDeclaredField(fieldName);
                fieldItem.setAccessible(true);
                final Object fieldObjectItem = fieldItem.get(item);
                fieldItem.setAccessible(false);
                if (fieldObjectItem == null) {
                    if (expectedFieldValue != null) {
                        mismatchDescription.appendText(fieldName + ": " + fieldObjectItem);
                    }
                } else if (!fieldObjectItem.equals(expectedFieldValue)) {
                    mismatchDescription.appendText(fieldName + ": " + fieldObjectItem);
                    return false;
                }
            }
            catch (IllegalAccessException  e) {
                mismatchDescription.appendText(fieldName + " is inaccessible");
                e.printStackTrace();
                return false;
            }
            catch (NoSuchFieldException e) {
                mismatchDescription.appendText(fieldName + " doesn't exist");
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText(fieldName + ": " + expectedFieldValue);
        }
    }
}