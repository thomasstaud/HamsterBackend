package at.ac.htlinn.hamsterbackend.program;

import at.ac.htlinn.hamsterbackend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProgramService {
    @Autowired
    ProgramRepository programRepository;

    @Autowired
    UserService userService;

    public boolean delete(long programId) {
        Optional<Program> o = programRepository.findById(programId);
        if (o.isPresent()) {
            Program p = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == p.getUserId()) {
                programRepository.delete(p);
                Optional<Program> o2 = programRepository.findById(programId);
                return o2.isEmpty();
            }
        }
        return false;
    }

    public Set<Program> getProgramBasicData(long id) {
        return programRepository.findAllProgramBasicDataByUserID(id);
    }

    public Program getProgram(long id) {
        return programRepository.findProgramByProgramId(id);
    }

    public ArrayList<Program> getProgramsByNames(ArrayList<String> names) {
        ArrayList<Program> programs = new ArrayList<>();
        names.forEach(n -> {
            programs.add(programRepository.findProgramByProgramName(n));
        });
        return programs;
    }

    public Set<Program> getAllNeededProgramsToRun(Program mainProgram) {
        Set<String> foundClasses = new HashSet<>();
        Set<Program> programs = new HashSet<>();
        Set<String> classNames = new HashSet<>(mainProgram.extractUsedExternalClasses());

        while (!classNames.isEmpty()) {
            classNames.forEach(n -> {
                Program program = programRepository.findProgramByProgramName(n);
                classNames.addAll(program.extractUsedExternalClasses());
                foundClasses.add(n);
            });
            classNames.removeIf(foundClasses::contains);
        }

        foundClasses.forEach(n -> programs.add(programRepository.findProgramByProgramName(n)));

        return programs;
    }

    public Program save(Program program) {
        return programRepository.save(program);
    }

    public boolean update(Program program) {
        Optional<Program> o = programRepository.findById(program.getProgramId());
        if (o.isPresent()) {
            Program p = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == p.getUserId()) {
                return programRepository.update(p.getProgramId(), p.getSourceCode());
            }
        }
        return false;
    }

    public boolean updatePath(Program program) {
        return programRepository.updatePath(program.getProgramId(), program.getProgramPath());
    }

    public boolean updateName(Program program) {
        return programRepository.updateName(program.getProgramId(), program.getProgramName());
    }
}
