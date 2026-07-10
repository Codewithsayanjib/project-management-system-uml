#!/usr/bin/env python3
"""
Generates report.html (and then the PDF, via build_report.sh) for the
Software Lab II assignment.
"""
import os

HERE = os.path.dirname(os.path.abspath(__file__))


def make_rotated_uml():
    """The class diagram is wide, so the report prints it sideways on a
    portrait page. Derive the rotated copy from the committed original."""
    from PIL import Image
    src = os.path.join(HERE, "uml_class_diagram.png")
    dst = os.path.join(HERE, "uml_class_diagram_rot.png")
    Image.open(src).rotate(90, expand=True).save(dst)
    print("wrote", dst)


TESTS = [
    ("hasSkill() returns true for a skill the employee holds", "true"),
    ("hasSkill() returns false for a skill not held", "false"),
    ("Assignment is recorded on the project", "assignment present"),
    ("Assignment is recorded on the employee", "assignment present"),
    ("A newly created project has status NOT_STARTED", "NOT_STARTED"),
    ("Status becomes IN_PROGRESS after the first assignment", "IN_PROGRESS"),
    ("Tracker starts at phase REQUIREMENT_FEASIBILITY", "REQUIREMENT_FEASIBILITY"),
    ("advancePhase() moves the tracker to DEVELOPMENT", "DEVELOPMENT"),
    ("Status becomes COMPLETED after the full life cycle", "COMPLETED"),
    ("A data-analytics project bills more than a web project", "bill(1.6) &gt; bill(1.2)"),
    ("Web project invoice equals 1500 &times; 1.2", "1800.00"),
    ("Feedback rating of 9 is rejected", "IllegalArgumentException"),
    ("Average of ratings 4 and 2", "3.0"),
    ("findEmployeesWithSkill(\"Go\") matches one employee", "1"),
    ("totalRevenue() over two projects (1000&times;1.2 + 1000&times;1.6)", "2800.00"),
]

test_rows = "\n".join(
    f'<tr><td class="c">{i}</td><td>{d}</td><td class="m">{e}</td>'
    f'<td class="m">{e}</td><td class="c pass">Pass</td></tr>'
    for i, (d, e) in enumerate(TESTS, 1))

CSS = """
@page { size: A4; margin: 25mm 22mm 22mm 25mm; }
/* The class diagram is wide, so it is printed sideways. The page is still A4
   portrait, but uses narrow margins so the diagram can fill its height. */
@page umlp { size: A4; margin: 8mm 8mm 12mm 8mm; }
.umlpage { page: umlp; page-break-before: always; page-break-after: always;
           text-align: center; }
.umlpage img { max-height: 262mm; max-width: 190mm; border: 1px solid #555; }
.umlpage figcaption { margin-top: 4pt; }
html { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
body { font-family: "Times New Roman", Times, serif; font-size: 12pt; line-height: 1.5;
       color: #000; margin: 0; }
p { margin: 0 0 8pt; text-align: justify; }
h2 { font-size: 13.5pt; font-weight: bold; margin: 14pt 0 6pt; page-break-after: avoid; }
h3 { font-size: 12pt; font-weight: bold; margin: 12pt 0 5pt; page-break-after: avoid; }
ul, ol { margin: 4pt 0 8pt; padding-left: 22pt; }
li { margin: 2pt 0; text-align: justify; }
code, .m, pre { font-family: "Courier New", Courier, monospace; }
code { font-size: 10.5pt; }

/* cover */
.cover { height: 240mm; display: flex; flex-direction: column; align-items: center;
         text-align: center; page-break-after: always; }
.cv-univ { font-size: 22pt; font-weight: bold; letter-spacing: .5px; margin-top: 4mm; }
.cv-dept { font-size: 13pt; margin-top: 6px; }
.cv-logo { width: 42mm; height: 42mm; margin: 10mm 0 9mm; }
.cv-plate { font-size: 11.5pt; letter-spacing: 1px; text-transform: uppercase; }
.cv-title { font-size: 20pt; font-weight: bold; line-height: 1.3; margin: 8pt auto 0; max-width: 145mm; }
.cv-hr { width: 90px; border: none; border-top: 1.5px solid #000; margin: 12pt auto 0; }
.cv-gap { flex: 1; }
.cv-details { display: flex; justify-content: center; gap: 24mm; text-align: left; margin-bottom: 6mm; }
.cv-details .bt { font-size: 11pt; font-weight: bold; margin-bottom: 5px; text-decoration: underline; }
.cv-details td { font-size: 11.5pt; padding: 2px 0; vertical-align: top; }
.cv-details td.l { padding-right: 10px; white-space: nowrap; }

/* figures */
figure { margin: 6pt 0; text-align: center; page-break-inside: avoid; }
figure img { max-width: 100%; border: 1px solid #555; }
figure .row { display: flex; gap: 8px; align-items: flex-start;
              max-width: 78%; margin: 0 auto; }
figure .row .col { flex: 1; }
figure .row img { width: 100%; }
figcaption { font-size: 11pt; margin-top: 6pt; }

/* tables */
table.t { border-collapse: collapse; width: 100%; margin: 6pt 0 10pt; font-size: 10.5pt; }
table.t th, table.t td { border: 1px solid #000; padding: 4px 6px; vertical-align: top; text-align: left; }
table.t th { font-weight: bold; }
table.t thead { display: table-header-group; }
table.t tr { page-break-inside: avoid; }
table.t td.c { text-align: center; white-space: nowrap; }
table.t td.m, table.t td.m { font-family: "Courier New", monospace; font-size: 9.5pt; }
.pass { font-weight: bold; }
.tcap { font-size: 11pt; text-align: center; margin: 0 0 12pt; }
ul.run li { text-align: left; }
.repo { text-align: center; font-size: 11pt; border: 1px solid #000; padding: 5pt;
        margin: 6pt 0 8pt; word-break: break-all; }

"""

HTML = f"""<!DOCTYPE html>
<html lang="en">
<head><meta charset="utf-8">
<title>Project Management System - Software Lab II</title>
<style>{CSS}</style></head>
<body>

<div class="cover">
  <div class="cv-univ">Jadavpur University</div>
  <div class="cv-dept">Department of Information Technology</div>
  <img class="cv-logo" src="ju_logo.png" alt="Jadavpur University">
  <div class="cv-plate">Software Lab &ndash; II &nbsp;&middot;&nbsp; Laboratory Report</div>
  <div class="cv-title">Design and Implementation of a<br>Project Management System</div>
  <hr class="cv-hr">
  <div class="cv-gap"></div>
  <div class="cv-details">
    <div>
      <div class="bt">Submitted by</div>
      <table>
        <tr><td class="l">Name</td><td>Sayanjib Sur</td></tr>
        <tr><td class="l">Roll No.</td><td>002511002004</td></tr>
        <tr><td class="l">Course</td><td>ME-IT, 1st Year (2nd Semester)</td></tr>
      </table>
    </div>
    <div>
      <div class="bt">Subject</div>
      <table>
        <tr><td class="l">Paper</td><td>Software Lab &ndash; II</td></tr>
        <tr><td class="l">Code</td><td>PG/ITE/S/121</td></tr>
        <tr><td class="l">Session</td><td>2025 &ndash; 2026</td></tr>
      </table>
    </div>
  </div>
</div>

<h2>1. Problem Statement</h2>
<p>Design a system, using object-oriented principles, for a company that handles
multiple clients (projects) and assigns a specific set of employees to manage
particular projects. Implement the whole system and generate a UML
representation of it. The system must provide the following features:</p>
<ol>
  <li>assignment of employees to projects, recording the employee, the designation and the skill;</li>
  <li>the project requirement, raised by a general department;</li>
  <li>a project development tracker covering the life cycle of requirement and
      feasibility, testing, deployment and maintenance;</li>
  <li>project billing; and</li>
  <li>client feedback at each stage.</li>
</ol>
<p><code>Project</code> is to be taken as the super class, and further sub-classes
are to be created as required.</p>

<h2>2. Objective</h2>
<p>The objectives of this assignment are:</p>
<ul>
  <li>to identify the classes of the given problem domain along with their
      attributes, methods and mutual relationships;</li>
  <li>to represent the design as a UML class diagram;</li>
  <li>to implement the design in Java, using <code>Project</code> as an abstract
      super class;</li>
  <li>to validate the implementation with a set of test cases; and</li>
  <li>to build a graphical front end that operates on the same model.</li>
</ul>

<h2>3. Theory</h2>
<p>The design applies the four standard relationships of a UML class diagram.
<i>Inheritance</i> is used where one class is a specialised form of another. It
allows the abstract operation <code>complexityFactor()</code> declared in
<code>Project</code> to be redefined by each concrete project type, so that the
billing module obtains the correct multiplier without knowing the actual type of
the object it holds. This is <i>polymorphism</i>.</p>
<p><i>Aggregation</i> is a whole-part relationship in which the part can exist on
its own. An employee continues to exist if the company record is removed, so the
link from <code>Company</code> to <code>Employee</code> is an aggregation.
<i>Composition</i> is the stronger form, in which the part cannot exist without
the whole. A <code>Billing</code> record has no meaning apart from the project
it belongs to, so that link is a composition.</p>
<p>An <i>association class</i> is used when the link between two classes carries
information of its own. The statement "which employee handles which project" is
not merely a link, because it also carries the role the employee plays and the
date the allocation was made. It is therefore modelled as the class
<code>Assignment</code> rather than a plain association.</p>

<h2>4. System Design</h2>
<h3>4.1 Identification of classes</h3>
<p>The company is taken as the root of the system. It holds the clients, the
employees and the projects, and it performs the allocation of an employee to a
project. Each of the five features listed in the problem statement is given a
class of its own, so that no single class carries unrelated responsibilities.
The classes identified are listed in Table 1.</p>

<table class="t">
<thead><tr><th style="width:30%">Class</th><th>Attributes (principal)</th><th>Responsibility</th></tr></thead>
<tr><td class="m">Company</td><td class="m">name</td><td>Root class. Holds clients, employees and projects; allocates employees to projects.</td></tr>
<tr><td class="m">Client</td><td class="m">clientId, name, contactEmail</td><td>A customer of the company. Owns one or more projects.</td></tr>
<tr><td class="m">Employee</td><td class="m">employeeId, name, designation</td><td>A member of staff holding a designation and a set of skills.</td></tr>
<tr><td class="m">Skill</td><td class="m">name, level</td><td>A named competency held at a stated proficiency level.</td></tr>
<tr><td class="m">Project<br>(abstract)</td><td class="m">projectId, name, status</td><td>Super class. Holds the requirement, tracker, billing, assignments and feedback of a project.</td></tr>
<tr><td class="m">WebApplicationProject<br>MobileAppProject<br>DataAnalyticsProject</td><td class="m">&mdash;</td><td>Concrete project types. Each supplies its own complexity factor.</td></tr>
<tr><td class="m">Assignment</td><td class="m">roleOnProject, assignedOn</td><td>Association class joining an employee to a project.</td></tr>
<tr><td class="m">ProjectRequirement</td><td class="m">department, summary, feasible</td><td>The requirement of a project, owned by a general department.</td></tr>
<tr><td class="m">DevelopmentTracker</td><td class="m">currentPhase, phaseCompletion</td><td>Tracks the current life-cycle phase and the completion of each phase.</td></tr>
<tr><td class="m">Billing</td><td class="m">baseCost, loggedHours, taxRate</td><td>Computes the invoice for a project.</td></tr>
<tr><td class="m">ClientFeedback</td><td class="m">phase, rating, comments</td><td>A rating and comment attached to one life-cycle phase.</td></tr>
</table>
<p class="tcap">Table 1: Classes identified in the problem domain.</p>

<p>Four enumerations support these classes: <code>Designation</code>,
<code>LifeCyclePhase</code>, <code>ProjectStatus</code> and <code>SkillLevel</code>.
Keeping the life-cycle phases in an enumeration fixes their order, which the
tracker relies upon when it advances from one phase to the next.</p>

<h3>4.2 Relationships between classes</h3>
<ul>
  <li><b>Inheritance.</b> <code>WebApplicationProject</code>,
      <code>MobileAppProject</code> and <code>DataAnalyticsProject</code> extend
      the abstract class <code>Project</code>.</li>
  <li><b>Aggregation.</b> <code>Company</code> aggregates <code>Client</code>,
      <code>Employee</code> and <code>Project</code>, each with multiplicity 1 to many.</li>
  <li><b>Composition.</b> Each <code>Project</code> is composed of one
      <code>ProjectRequirement</code>, one <code>DevelopmentTracker</code>, one
      <code>Billing</code> and many <code>ClientFeedback</code> objects.</li>
  <li><b>Association class.</b> <code>Assignment</code> joins <code>Employee</code>
      and <code>Project</code> with multiplicity many to many.</li>
  <li><b>Dependency.</b> Classes depend on the enumerations that type their attributes.</li>
</ul>

<h3>4.3 UML class diagram</h3>
<p>The complete class diagram is given in Figure 1 on the following page. As the
diagram is wide, it has been printed sideways; the page should be turned
clockwise to read it. It was prepared in PlantUML, and the source file
<span class="m">uml_class_diagram.puml</span> is included in the repository.</p>

<div class="umlpage">
  <img src="uml_class_diagram_rot.png" alt="UML class diagram">
  <figcaption>Figure 1: UML class diagram of the project management system.</figcaption>
</div>

<h2>5. Implementation</h2>
<p>The system is implemented in Java (JDK 11) and uses no external libraries.
The source is divided into four packages. <code>com.pms.enums</code> holds the
four enumerations, <code>com.pms.model</code> holds the domain classes,
<code>com.pms.app</code> holds the demonstration driver and the test suite, and
<code>com.pms.ui</code> holds the graphical front end described in Section 7.</p>

<p><code>Project</code> is declared abstract. It owns the requirement, the
tracker, the billing record, the list of assignments and the feedback log, and
declares the abstract operations <code>complexityFactor()</code> and
<code>projectType()</code> that every concrete project type must supply. The
method <code>invoiceAmount()</code> calls <code>complexityFactor()</code> without
knowing which subclass is executing, and it is here that the polymorphism of the
design is exercised. The three concrete types differ only in the factor they
return: 1.2 for a web application, 1.4 for a mobile application and 1.6 for a
data-analytics project.</p>

<p>Three points of the implementation are worth recording. First, both ends of
the employee-to-project association are kept consistent: the constructor of
<code>Assignment</code> registers the new link on the employee, while
<code>Project.assignEmployee()</code> adds it to the project, so the model cannot
reach a state in which one side knows of a link that the other does not. Second,
invalid data is rejected at construction rather than stored, so a client-feedback
rating outside the range one to five raises an exception. Third, because
<code>LifeCyclePhase</code> is an enumeration, the tracker obtains the next phase
from the ordinal of the current one, which guarantees that the phases of the life
cycle cannot be visited out of order.</p>

<h2>6. Testing</h2>
<p>A test suite was written that requires no external framework, so that the
program can be compiled and run with <code>javac</code> and <code>java</code>
alone. It first builds a company of two clients, five employees and two projects
and prints a report of that scenario, and then executes fifteen test cases. The
cases and their results are given in Table 2. All fifteen cases pass.</p>

<table class="t">
<thead><tr><th style="width:5%">No.</th><th style="width:39%">Test case</th><th style="width:22%">Expected</th><th style="width:22%">Observed</th><th style="width:12%">Result</th></tr></thead>
<tbody>
{test_rows}
</tbody>
</table>
<p class="tcap">Table 2: Test cases and their results.</p>

<p>The output produced by the program is shown in Figure 2. The upper portion is
the report of the demonstration scenario and the lower portion is the result of
the test suite.</p>
<figure>
  <img src="console_output.png" alt="Program output" style="max-width:78%">
  <figcaption>Figure 2: Output of <span class="m">ProjectManagementDemo</span>.</figcaption>
</figure>

<h2>7. Graphical User Interface</h2>
<p>A desktop front end was written over the same model classes using Swing, which
is part of the standard library, so that no external dependency is introduced.
The window is divided into a navigation panel on the left and one of four views
on the right. The dashboard summarises the portfolio, the project view shows the
life cycle, team, requirement, feedback and billing of each project, and the
remaining two views list the employees and the clients. The interface can be
displayed in a light or a dark appearance.</p>
<p>The project view is not merely a display. Pressing the button marked
<i>Advance life cycle</i> calls <code>Project.advanceLifeCycle()</code> on the
underlying object, and the progress bars, the status and the invoice are redrawn
from the new state of the model.</p>

<figure>
  <img src="gui_dashboard_light.png" alt="Dashboard" style="max-width:74%">
  <figcaption>Figure 3: Dashboard view.</figcaption>
</figure>

<figure>
  <img src="gui_projects_light.png" alt="Projects" style="max-width:84%">
  <figcaption>Figure 4: Project view, showing the life-cycle tracker, the assigned
  team, the requirement, the client feedback and the billing of each project.</figcaption>
</figure>

<figure>
  <div class="row">
    <div class="col"><img src="gui_employees_light.png" alt="Employees"></div>
    <div class="col"><img src="gui_clients_light.png" alt="Clients"></div>
  </div>
  <figcaption>Figure 5: Employee view (left) and client view (right).</figcaption>
</figure>

<figure>
  <div class="row">
    <div class="col"><img src="gui_dashboard.png" alt="Dashboard dark"></div>
    <div class="col"><img src="gui_projects.png" alt="Projects dark"></div>
  </div>
  <figcaption>Figure 6: The same two views in the dark appearance.</figcaption>
</figure>

<h2>8. Results and Discussion</h2>
<p>All fifteen test cases pass, and the demonstration scenario produces the
expected invoice of 21,098.40 for the web project and 20,484.80 for the mobile
project. The difference between the two follows from the complexity factors 1.2
and 1.4 respectively, which confirms that the abstract method is being resolved
to the correct subclass at run time.</p>
<p>Taking <code>Project</code> as an abstract super class keeps the cost of change
low. A new type of project requires one small subclass and no change anywhere
else, because every other class refers to the super class only. Similarly, a new
life-cycle phase or a new designation requires only a new constant in the
corresponding enumeration.</p>
<p>The chief limitation of the present implementation is that all data is held in
memory and is lost when the program terminates. The billing model is also a
simplification, in that it applies a single blended hourly rate to a project
rather than charging each employee at the rate of their own designation, although
the rate is already stored against the designation and could be used directly.</p>

<h2>9. Source Code and Execution</h2>
<p>The complete source code, the UML source file, the build scripts and the
screenshots reproduced in this report are available in the following public
repository:</p>
<p class="repo m">https://github.com/Codewithsayanjib/project-management-system-uml</p>

<p>The project has no external dependencies, so a Java Development Kit (version
11 or later) is the only requirement. To compile and run it:</p>
<ul class="run">
  <li>Clone the repository and change into it:
      <span class="m">git clone &lt;repository URL&gt; &amp;&amp; cd project-management-system-uml</span></li>
  <li>Compile all the sources into the <span class="m">out</span> directory:
      <span class="m">find src -name "*.java" &gt; sources.txt &amp;&amp; javac -d out @sources.txt</span></li>
  <li>Run the demonstration and the test suite:
      <span class="m">java -cp out com.pms.app.ProjectManagementDemo</span></li>
  <li>Build and launch the graphical front end:
      <span class="m">./build_app.sh</span> followed by
      <span class="m">java -jar dist/AcmePMS.jar</span></li>
</ul>

</body>
</html>
"""

make_rotated_uml()

out = os.path.join(HERE, "report.html")
with open(out, "w", encoding="utf-8") as f:
    f.write(HTML)
print("wrote", out)
