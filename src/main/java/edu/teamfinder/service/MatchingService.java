package edu.teamfinder.service;

import edu.teamfinder.dto.ApiDtos.CandidateSummary;
import edu.teamfinder.entity.*;
import edu.teamfinder.entity.Enums.*;
import edu.teamfinder.exception.ApiException;
import edu.teamfinder.repository.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {
    private final ProjectService projects; private final StudentRepository students; private final TeamMemberRepository members; private final TeamRequestRepository requests;
    public MatchingService(ProjectService projects,StudentRepository students,TeamMemberRepository members,TeamRequestRepository requests){this.projects=projects;this.students=students;this.members=members;this.requests=requests;}
    @Transactional(readOnly=true) public List<CandidateSummary> candidates(String email,Long projectId) {
        Project project=projects.require(projectId); if(!project.getOwner().getEmail().equalsIgnoreCase(email)) throw ApiException.forbidden("Only the project owner can view candidate matches.");
        Set<String> required=names(project,SkillType.REQUIRED), preferred=names(project,SkillType.PREFERRED);
        return students.findAll().stream().filter(s->s.getRole()==Role.STUDENT && s.getAvailability()!=Availability.UNAVAILABLE)
            .filter(s->!members.existsByProjectIdAndStudentId(projectId,s.getId()))
            .filter(s->!requests.existsByProjectIdAndToStudentIdAndStatus(projectId,s.getId(),RequestStatus.PENDING))
            .map(s->score(s,project,required,preferred)).filter(c->c.matchPercentage()>0).sorted(Comparator.comparing(CandidateSummary::matchPercentage).reversed().thenComparing(CandidateSummary::name)).toList();
    }
    public CandidateSummary score(Student student,Project project,Set<String> required,Set<String> preferred) {
        Set<String> studentSkills=student.getSkills().stream().map(x->normal(x.getSkill().getName())).collect(Collectors.toSet());
        List<String> matches=new ArrayList<>(); required.stream().filter(studentSkills::contains).forEach(matches::add); preferred.stream().filter(studentSkills::contains).filter(x->!matches.contains(x)).forEach(matches::add);
        List<String> missing=required.stream().filter(x->!studentSkills.contains(x)).toList();
        double requiredPart=required.isEmpty()?80d:80d*required.stream().filter(studentSkills::contains).count()/required.size();
        double preferredPart=preferred.isEmpty()?15d:15d*preferred.stream().filter(studentSkills::contains).count()/preferred.size();
        boolean interest=Arrays.stream(Optional.ofNullable(student.getInterests()).orElse("").split(",")).map(MatchingService::normal).anyMatch(i->i.equals(normal(project.getCategory())));
        int score=(int)Math.round(requiredPart+preferredPart+(interest?5:0));
        String explanation=explain(score,matches,missing,interest); return new CandidateSummary(student.getId(),student.getFullName(),student.getBranch(),student.getAcademicYear(),score,title(matches),title(missing),explanation);
    }
    private static String explain(int score,List<String> matched,List<String> missing,boolean interest){String lead=score+"% match - "; if(matched.isEmpty()) return lead+(interest?"Project-domain interest aligns, but required skills are missing.":"No required skills match yet.");String base=String.join(", ",title(matched))+" match";return lead+base+(missing.isEmpty()?" the project requirements.":"; missing "+String.join(", ",title(missing))+".");}
    private static Set<String> names(Project p,SkillType type){return p.getSkills().stream().filter(x->x.getSkillType()==type).map(x->normal(x.getSkill().getName())).collect(Collectors.toCollection(LinkedHashSet::new));}
    private static String normal(String v){return v.trim().toLowerCase(Locale.ROOT);} private static List<String> title(Collection<String> v){return v.stream().map(s->Arrays.stream(s.split(" ")).map(x->x.isEmpty()?x:Character.toUpperCase(x.charAt(0))+x.substring(1)).collect(Collectors.joining(" "))).toList();}
}
