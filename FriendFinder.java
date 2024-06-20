
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FriendFinder {
	
	protected ClassesDataSource classesDataSource;
	protected StudentsDataSource studentsDataSource;
	
	public FriendFinder(ClassesDataSource cds, StudentsDataSource sds) {
		if (cds == null || sds == null) {
			throw new IllegalArgumentException("Data sources can't be null");
		}
		classesDataSource = cds;
		studentsDataSource = sds;
	}
	
	/*
	 * This method takes a String representing the name of a student 
	 * and then returns a set containing the names of everyone else 
	 * who is taking the same classes as that student.
	 */
	public Set<String> findClassmates(Student theStudent) {
		if (theStudent == null) {
			throw new IllegalArgumentException("The student's name can't be null");
		}
		String name = theStudent.getName();
		if (name == null || name.isEmpty()) {
			throw new IllegalStateException("Student name can't be null or empty");
		}
		// find the classes that this student is taking
		List<String> myClasses = classesDataSource.getClasses(name);
		if (myClasses == null) {
			return Collections.emptySet(); // return empty Set if the student isn't taking any classes
		}
		
		// use the classes to find the names of the students
		Set<String> classmates = new HashSet<String>();
		
		for (String myClass : myClasses) {
			// list all the students in the class
			List<Student> students = studentsDataSource.getStudents(myClass);
			
			//check for null 
			if (students == null) {
				throw new IllegalStateException("The list of students is null");
			}
			for (Student otherStudent : students) {
				//ignore null students 
				
				if (otherStudent == null || otherStudent.getName() == null) {
					continue;
				}
				
				// find the other classes that they're taking
				List<String> theirClasses = classesDataSource.getClasses(otherStudent.getName());
				//check if their Classes is null 
				if (theirClasses == null) {
					throw new IllegalStateException("The list of classes is null");
				}

				// see if all of the classes that they're taking are the same as the ones this student is taking
				boolean allSame = true;
				for (String c : myClasses) {
					if (theirClasses.contains(c) == false) {
						allSame = false;
						break;
					}
				}
				
				// if they're taking all the same classes, then add to the set of classmates
				if (allSame) {
					if (otherStudent.getName().equals(name) == false) {
						classmates.add(otherStudent.getName());
					}
				}
			}

		}
				
		return classmates;
	}
	

}
